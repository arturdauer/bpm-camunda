package de.hello.first.backend.v1.rest;

import de.hello.first.v1.mitarbeiteraendern.model.Mitarbeiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class MitarbeiterAendernService {

    private static final String BACKEND_AUTH_HEADER_KEY = "Backend-Authorization";
    private static final String ACCEPT_HEADER_VALUE = "application/json";

    private final RestTemplate restTemplate;
    private final String endpoint;
    private final String user;
    private final String password;

    @Autowired
    public MitarbeiterAendernService(final RestTemplate restTemplate,
                                     @Value("${service.maaendern.endpoint}") final String endpoint,
                                     @Value("${service.maaendern.user}") final String user,
                                     @Value("${service.maaendern.password}") final String password) {
        this.restTemplate = restTemplate;
        this.endpoint = endpoint;
        this.user = user;
        this.password = password;
    }

    public Boolean aendereMitarbeiter(Mitarbeiter mitarbeiter) {
        final HttpEntity<Mitarbeiter> request = new HttpEntity<>(mitarbeiter, getHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(endpoint, HttpMethod.POST, request, String.class);
        if (HttpStatus.OK == responseEntity.getStatusCode()) {
            String a = responseEntity.getBody();
        }
        return (HttpStatus.OK == responseEntity.getStatusCode());
    }

    private HttpHeaders getHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, ACCEPT_HEADER_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT, ACCEPT_HEADER_VALUE);
        httpHeaders.setBasicAuth(user, password, StandardCharsets.UTF_8);
        httpHeaders.set(BACKEND_AUTH_HEADER_KEY, "Basic " + Base64.getEncoder().encodeToString( //
                String.format("%s:%s", "user", "password").getBytes(StandardCharsets.UTF_8)));
        return httpHeaders;
    }

}
