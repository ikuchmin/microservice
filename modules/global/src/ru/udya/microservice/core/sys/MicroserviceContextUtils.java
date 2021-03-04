package ru.udya.microservice.core.sys;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.BeanDefinitionStoreException;

import java.io.FileNotFoundException;
import java.util.function.Supplier;

public class MicroserviceContextUtils {

    public static <R> R ignoreFileNotFoundException(Supplier<R> fn, Log logger) {
        try {
            return fn.get();
        } catch (BeanDefinitionStoreException e) {
            Throwable cause = e.getCause();

            if (cause instanceof FileNotFoundException) {
                logger.warn(cause.getMessage());
            } else {
                throw e;
            }
        }

        return null;
    }
}
