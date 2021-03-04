package ru.udya.microservice.restapi;

import com.haulmont.addon.restapi.api.rest.RestAPIDispatcherServlet;
import ru.udya.microservice.core.sys.MicroserviceCubaXmlWebApplicationContext;

public class MicroserviceRestAPIDispatcherServlet extends RestAPIDispatcherServlet {

    @Override
    public Class<?> getContextClass() {
        return MicroserviceCubaXmlWebApplicationContext.class;
    }
}
