package de.hello.first.v1.firstBpm.inbound.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

/**
 * Konfig für das Empfangen der SOAP-Anfragen.
 */
@EnableWs
@Configuration
@Component
public class WebServiceAdapterConfig extends WsConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceAdapterConfig.class);

    @Value("${incomingService.soapCallUrl:/ws/*}")
    private String soapCallUrl;

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        LOGGER.info("+++");
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, soapCallUrl);
    }

    /**
     * URL-Pattern für Anfragen, die als SOAP-Anrufe d
     * @return
     */
    public String getSoapCallUrl() {
        return soapCallUrl;
    }
}
