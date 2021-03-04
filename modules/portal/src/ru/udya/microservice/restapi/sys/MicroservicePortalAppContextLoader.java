package ru.udya.microservice.restapi.sys;

import com.haulmont.cuba.core.sys.CubaXmlWebApplicationContext;
import com.haulmont.cuba.core.sys.ServletContextHolder;
import com.haulmont.cuba.portal.sys.PortalAppContextLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import ru.udya.microservice.core.sys.MicroserviceCubaXmlWebApplicationContext;

import javax.servlet.ServletContext;

public class MicroservicePortalAppContextLoader extends PortalAppContextLoader {
    @Override
    protected ApplicationContext createApplicationContext(String[] locations, ServletContext servletContext) {
        CubaXmlWebApplicationContext webContext = new MicroserviceCubaXmlWebApplicationContext();

        String[] classPathLocations = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            classPathLocations[i] = "classpath:" + locations[i];
        }
        webContext.setConfigLocations(classPathLocations);
        webContext.setServletContext(ServletContextHolder.getServletContext());
        webContext.refresh();

        if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
            throw new IllegalStateException(
                    "Cannot initialize context because there is already a root application context present - " +
                            "check whether you have multiple ContextLoader* definitions in your web.xml!");
        }
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webContext);

        return webContext;
    }
}
