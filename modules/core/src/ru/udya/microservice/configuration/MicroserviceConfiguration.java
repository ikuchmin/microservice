package ru.udya.microservice.configuration;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.security.sys.TrustedLoginHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.udya.microservice.sys.CustomTrustedLoginHandler;

@Configuration
public class MicroserviceConfiguration {

    @Bean(TrustedLoginHandler.NAME)
    public TrustedLoginHandler trustedLoginHandler(ServerConfig serverConfig) {
        return new CustomTrustedLoginHandler(serverConfig);
    }
}
