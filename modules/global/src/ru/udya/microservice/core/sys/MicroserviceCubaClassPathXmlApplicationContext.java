package ru.udya.microservice.core.sys;

import com.haulmont.cuba.core.sys.CubaClassPathXmlApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;

import javax.servlet.ServletContext;
import java.io.IOException;

import static ru.udya.microservice.core.sys.MicroserviceContextUtils.ignoreFileNotFoundException;

public class MicroserviceCubaClassPathXmlApplicationContext
        extends CubaClassPathXmlApplicationContext {
    public MicroserviceCubaClassPathXmlApplicationContext(String[] locations) {
        super(locations);
    }

    public MicroserviceCubaClassPathXmlApplicationContext(String[] locations, ServletContext sc) {
        super(locations, sc);
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
