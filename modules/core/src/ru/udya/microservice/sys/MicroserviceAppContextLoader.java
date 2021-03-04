package ru.udya.microservice.sys;

import com.haulmont.cuba.core.sys.AppContextLoader;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;

public class MicroserviceAppContextLoader extends AppContextLoader {

    @Override
    protected ClassPathXmlApplicationContext createApplicationContext(String[] locations) {
        return new MicroserviceCoreApplicationContext(locations);
    }

    @Override
    protected ClassPathXmlApplicationContext createApplicationContext(String[] locations, ServletContext servletContext) {
        return new MicroserviceCoreApplicationContext(locations, servletContext);
    }
}
