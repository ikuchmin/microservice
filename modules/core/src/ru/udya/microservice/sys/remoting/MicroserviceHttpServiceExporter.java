package ru.udya.microservice.sys.remoting;

import com.google.common.base.Joiner;
import com.haulmont.cuba.core.sys.remoting.CubaRemoteInvocationExecutor;
import com.haulmont.cuba.core.sys.serialization.SerializationException;
import com.haulmont.cuba.core.sys.serialization.SerializationSupport;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

public class MicroserviceHttpServiceExporter extends HttpInvokerServiceExporter {

    public MicroserviceHttpServiceExporter() {
        super();
        setRegisterTraceInterceptor(false);
        setRemoteInvocationExecutor(new CubaRemoteInvocationExecutor());
    }

    @Override
    public void setService(Object service) {
        super.setService(service);
    }

    /*
     * In base implementation, exceptions which are thrown during reading remote invocation request, are not handled.
     * Client gets useless HTTP status 500 in this case.
     *
     * This implementation passes some known exceptions to client.
     */
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RemoteInvocationResult result;
        RemoteInvocation invocation = null;
        try {
            invocation = readRemoteInvocation(request);
            result = invokeAndCreateResult(invocation, getProxy());
        } catch (OptionalDataException | ClassCastException e) { // typical binary incompatibility exceptions
            logger.error("Failed to read remote invocation request", e);
            result = new RemoteInvocationResult(e);
        } catch (ClassNotFoundException ex) {
            throw new NestedServletException("Class not found during deserialization", ex);
        }
        try {
            writeRemoteInvocationResult(request, response, result);
        } catch (SerializationException e) {
            String serviceName = null;
            if (getServiceInterface() != null) {
                serviceName = getServiceInterface().getSimpleName();
            }
            String methodName = null;
            String paramTypes = null;
            if (invocation != null) {
                methodName = invocation.getMethodName();
                if (invocation.getParameterTypes() != null) {
                    paramTypes = Joiner.on(",").join(invocation.getParameterTypes());
                }
            }
            throw new NestedServletException(
                    String.format("Failed to write result for service [%s.%s(%s)]", serviceName, methodName, paramTypes), e);
        }
    }

    @Override
    protected void doWriteRemoteInvocationResult(RemoteInvocationResult result, ObjectOutputStream oos) throws IOException {
        SerializationSupport.serialize(result, oos);
    }

    @Override
    protected RemoteInvocation doReadRemoteInvocation(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (RemoteInvocation) SerializationSupport.deserialize(ois);
    }
}
