package ru.udya.microservice.sys.remoting;

import com.haulmont.cuba.core.sys.remoting.HttpServiceExporter;
import com.haulmont.cuba.core.sys.remoting.RemoteServicesBeanCreator;
import com.haulmont.cuba.core.sys.remoting.ServiceExportHelper;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MicroserviceRemoteServicesBeanCreator extends RemoteServicesBeanCreator {

    private static final Logger log = LoggerFactory.getLogger(MicroserviceRemoteServicesBeanCreator.class);

    private ApplicationContext context;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(@Nonnull ConfigurableListableBeanFactory beanFactory)
            throws BeansException {

        log.info("Configuring remote services");

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

        ApplicationContext coreContext = context.getParent();
        if (coreContext == null) {
            throw new RuntimeException("Parent Spring context is null");
        }

        Map<String,Object> services = coreContext.getBeansWithAnnotation(Service.class);
        for (Map.Entry<String, Object> entry : services.entrySet()) {
            String serviceName = entry.getKey();
            Object service = entry.getValue();

            List<Class> serviceInterfaces = new ArrayList<>();
            List<Class<?>> interfaces = ClassUtils.getAllInterfaces(service.getClass());
            for (Class intf : interfaces) {
                if (intf.getName().endsWith("Service"))
                    serviceInterfaces.add(intf);
            }
            String intfName = null;
            if (serviceInterfaces.size() == 0) {
                log.error("Bean " + serviceName + " has @Service annotation but no interfaces named '*Service'. Ignoring it.");
            } else if (serviceInterfaces.size() > 1) {
                intfName = findLowestSubclassName(serviceInterfaces);
                if (intfName == null)
                    log.error("Bean " + serviceName + " has @Service annotation and more than one interface named '*Service', " +
                              "but these interfaces are not from the same hierarchy. Ignoring it.");
            } else {
                intfName = serviceInterfaces.get(0).getName();
            }

            if (intfName != null) {
                if (ServiceExportHelper.exposeServices()) {
                    BeanDefinition definition = new RootBeanDefinition(HttpServiceExporter.class);
                    MutablePropertyValues propertyValues = definition.getPropertyValues();
                    propertyValues.add("service", service);
                    propertyValues.add("serviceInterface", intfName);
                    registry.registerBeanDefinition("/" + serviceName, definition);
                    log.debug("Bean " + serviceName + " configured for export via HTTP");
                } else {

                    // begin: EXT. Register external endpoints during local registration

                    BeanDefinition definition = new RootBeanDefinition(MicroserviceHttpServiceExporter.class);
                    MutablePropertyValues propertyValues = definition.getPropertyValues();
                    propertyValues.add("service", service);
                    propertyValues.add("serviceInterface", intfName);
                    registry.registerBeanDefinition("/" + serviceName, definition);
                    log.debug("Bean " + serviceName + " configured for export via HTTP");

                    // end: EXT

                    ServiceExportHelper.registerLocal("/" + serviceName, service);
                }
            }
        }
    }
}
