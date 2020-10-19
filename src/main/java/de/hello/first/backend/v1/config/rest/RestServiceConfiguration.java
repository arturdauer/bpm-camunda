package de.hello.first.backend.v1.config.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.home.logging.OutgoingRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestServiceConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        restTemplate.getInterceptors().add(new OutgoingRequestInterceptor());
        restTemplate.getMessageConverters().add(0, new MappingJackson2HttpMessageConverter(createObjectMapper()));
        return restTemplate;
    }

    private ObjectMapper createObjectMapper() {
        return new Jackson2ObjectMapperBuilder() //
                .modules(new JavaTimeModule()) //
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //
                .serializationInclusion(JsonInclude.Include.NON_NULL) //
                .build();
    }
}
