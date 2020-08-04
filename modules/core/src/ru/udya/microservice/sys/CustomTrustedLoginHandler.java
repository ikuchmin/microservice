package ru.udya.microservice.sys;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.security.sys.TrustedLoginHandler;

public class CustomTrustedLoginHandler extends TrustedLoginHandler {

    public CustomTrustedLoginHandler(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public boolean checkAddress(String address) {
        return true;
    }
}
