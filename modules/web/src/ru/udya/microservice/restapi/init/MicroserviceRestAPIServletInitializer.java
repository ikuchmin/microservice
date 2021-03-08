package ru.udya.microservice.restapi.init;

import com.haulmont.addon.restapi.init.RestAPIServletInitializer;
import com.haulmont.cuba.core.sys.AbstractWebAppContextLoader;
import com.haulmont.cuba.core.sys.servlet.events.ServletContextInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.FrameworkServlet;
import ru.udya.microservice.restapi.MicroserviceRestAPIDispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

/**
 * DO NOT FORGET THE SAME CLASS IN PORTAL MODULE
 */
public class MicroserviceRestAPIServletInitializer extends RestAPIServletInitializer {

    @EventListener
    protected void init(ServletContextInitializedEvent event) {
        ApplicationContext appCtx = event.getApplicationContext();
        Servlet servlet = servletRegistrationManager.createServlet(appCtx, MicroserviceRestAPIDispatcherServlet.class.getName());

        ServletContext servletCtx = event.getSource();
        try {
            servlet.init(new AbstractWebAppContextLoader.CubaServletConfig(SERVLET_NAME, servletCtx));
        } catch (ServletException e) {
            throw new RuntimeException("An error occurred while initializing " + SERVLET_NAME + " servlet", e);
        }

        servletCtx.addServlet(SERVLET_NAME, servlet).addMapping(SERVLET_MAPPING);

        DelegatingFilterProxy springSecurityFilterChain = new DelegatingFilterProxy();
        springSecurityFilterChain.setContextAttribute(FrameworkServlet.SERVLET_CONTEXT_PREFIX + SERVLET_NAME);
        springSecurityFilterChain.setTargetBeanName("springSecurityFilterChain");

        FilterRegistration.Dynamic springSecurityFilterChainReg =
                servletCtx.addFilter("restapiSpringSecurityFilterChain", springSecurityFilterChain);

        springSecurityFilterChainReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, SERVLET_MAPPING);
    }
}