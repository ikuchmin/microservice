package ru.udya.microservice.sys.remoting;

import com.haulmont.cuba.core.sys.remoting.RemotingServlet;
import ru.udya.microservice.core.sys.MicroserviceCubaXmlWebApplicationContext;

public class MicroserviceRemotingServlet extends RemotingServlet {

    @Override
    public Class<?> getContextClass() {
        return MicroserviceCubaXmlWebApplicationContext.class;
    }
}
