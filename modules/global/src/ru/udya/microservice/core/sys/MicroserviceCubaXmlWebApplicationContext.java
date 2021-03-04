package ru.udya.microservice.core.sys;

import com.haulmont.cuba.core.sys.CubaXmlWebApplicationContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import java.io.IOException;

import static ru.udya.microservice.core.sys.MicroserviceContextUtils.ignoreFileNotFoundException;

public class MicroserviceCubaXmlWebApplicationContext extends CubaXmlWebApplicationContext {

    @Override
    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws IOException {
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            for (String configLocation : configLocations) {
                ignoreFileNotFoundException(() -> reader.loadBeanDefinitions(configLocation), logger);
            }
        }
    }
}
