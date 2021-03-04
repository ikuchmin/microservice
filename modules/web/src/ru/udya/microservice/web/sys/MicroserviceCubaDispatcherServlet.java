package ru.udya.microservice.web.sys;

import com.haulmont.cuba.web.sys.CubaDispatcherServlet;
import ru.udya.microservice.core.sys.MicroserviceCubaXmlWebApplicationContext;

import javax.annotation.Nonnull;

public class MicroserviceCubaDispatcherServlet extends CubaDispatcherServlet {

    @Nonnull
    @Override
    public Class<?> getContextClass() {
        return MicroserviceCubaXmlWebApplicationContext.class;
    }
}
