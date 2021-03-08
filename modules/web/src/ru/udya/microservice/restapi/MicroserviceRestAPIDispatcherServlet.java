package ru.udya.microservice.restapi;

import com.haulmont.addon.restapi.api.rest.RestAPIDispatcherServlet;
import ru.udya.microservice.core.sys.MicroserviceCubaXmlWebApplicationContext;

/**
 * DO NOT FORGET THE SAME CLASS IN PORTAL MODULE
 */
public class MicroserviceRestAPIDispatcherServlet extends RestAPIDispatcherServlet {

    @Override
    public Class<?> getContextClass() {
        return MicroserviceCubaXmlWebApplicationContext.class;
    }
}
