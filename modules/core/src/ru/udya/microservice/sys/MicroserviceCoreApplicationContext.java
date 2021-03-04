package ru.udya.microservice.sys;

import com.haulmont.cuba.core.sys.CubaCoreApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;

import javax.servlet.ServletContext;
import java.io.IOException;

import static ru.udya.microservice.core.sys.MicroserviceContextUtils.ignoreFileNotFoundException;

public class MicroserviceCoreApplicationContext extends CubaCoreApplicationContext {

    public MicroserviceCoreApplicationContext(String[] locations) {
        super(locations);
    }

    public MicroserviceCoreApplicationContext(String[] locations, ServletContext servletContext) {
        super(locations, servletContext);
    }

    @Override
    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
        Resource[] configResources = getConfigResources();
        if (configResources != null) {
            for (Resource configResource : configResources) {
                ignoreFileNotFoundException(() ->
                        reader.loadBeanDefinitions(configResource), logger);
            }
        }

        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            for (String configLocation : configLocations) {
                ignoreFileNotFoundException(() ->
                        reader.loadBeanDefinitions(configLocation), logger);
            }
        }
    }


}
