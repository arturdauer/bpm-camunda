package de.hello.first.backend.v1.config.soap;

import de.home.logging.CxfInLogEventSender;
import de.home.logging.CxfOutLogEventSender;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.ConnectionType;

public abstract class AbstractSoapClient<T> {

    protected abstract String getEndpoint();
    protected abstract String getUserName();
    protected abstract String getPassword();

    protected T createPortType(Class<T> portTypeClass) {
        JaxWsProxyFactoryBean jaxWsPortProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsPortProxyFactoryBean.setAddress(getEndpoint());
        jaxWsPortProxyFactoryBean.setServiceClass(portTypeClass);
        jaxWsPortProxyFactoryBean.setBindingId(Soap12.getInstance().getBindingId());
        jaxWsPortProxyFactoryBean.getOutInterceptors().add(new LoggingOutInterceptor(new CxfOutLogEventSender()));
        jaxWsPortProxyFactoryBean.getInInterceptors().add(new LoggingInInterceptor(new CxfInLogEventSender(false)));
        jaxWsPortProxyFactoryBean.getInFaultInterceptors().add(new LoggingInInterceptor(new CxfInLogEventSender(true)));

        T portType = (T) jaxWsPortProxyFactoryBean.create();
        configureClientConduit(portType);

        return portType;
    }

    protected void configureClientConduit(T portType) {
        Client client = ClientProxy.getClient(portType);
        configureClientHttpConduit((HTTPConduit) client.getConduit());
    }

    protected HTTPConduit configureClientHttpConduit(HTTPConduit conduit) {
        conduit.getAuthorization().setAuthorization("Basic");
        conduit.getAuthorization().setUserName(getUserName());
        conduit.getAuthorization().setPassword(getPassword());

        conduit.getClient().setConnectionTimeout(600000L);
        conduit.getClient().setReceiveTimeout(600000L);

        conduit.getClient().setConnection(ConnectionType.CLOSE);
        return conduit;
    }
}
