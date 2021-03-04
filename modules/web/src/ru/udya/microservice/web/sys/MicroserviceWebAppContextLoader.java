package ru.udya.microservice.web.sys;

import com.haulmont.cuba.web.sys.WebAppContextLoader;
import org.springframework.context.ApplicationContext;
import ru.udya.microservice.core.sys.MicroserviceCubaClassPathXmlApplicationContext;

import javax.servlet.ServletContext;

public class MicroserviceWebAppContextLoader extends WebAppContextLoader {

    @Override
    protected ApplicationContext createApplicationContext(String[] locations) {
        return new MicroserviceCubaClassPathXmlApplicationContext(locations);
    }

    @Override
    protected ApplicationContext createApplicationContext(String[] locations, ServletContext servletContext) {
        return new MicroserviceCubaClassPathXmlApplicationContext(locations, servletContext);
    }
}
