package ru.udya.microservice.portal.sys;

import com.haulmont.cuba.portal.sys.PortalDispatcherServlet;
import ru.udya.microservice.core.sys.MicroserviceCubaXmlWebApplicationContext;

public class MicroservicePortalDispatcherServlet extends PortalDispatcherServlet {

    @Override
    public Class<?> getContextClass() {
        return MicroserviceCubaXmlWebApplicationContext.class;
    }
}
