package de.hello.first.backend.v1.rest;

import com.sun.codemodel.JTryBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class VsnrAendernService {

    private static final String BACKEND_AUTH_HEADER_KEY = "Backend-Authorization";
    private static final String ACCEPT_HEADER_VALUE = "application/json";

    private final RestTemplate restTemplate;
    private final String endpoint;
    private final String user;
    private final String password;

    @Autowired
    public VsnrAendernService(final RestTemplate restTemplate,
                              @Value("${service.vsnraendern.endpoint}") final String endpoint,
                              @Value("${service.vsnraendern.user}") final String user,
                              @Value("${service.vsnraendern.password}") final String password) {
        this.restTemplate = restTemplate;
        this.endpoint = endpoint;
        this.user = user;
        this.password = password;
    }

    public String aendereVsnr(de.hello.first.v1.mitarbeiteraendern.model.Mitarbeiter mitarbeiter) {
        String vsnr = mitarbeiter.getId().toString();
        String ag = "70";
        String status = "";
        try {
            final ResponseEntity<String> responseEntity = restTemplate.exchange(getUrl(endpoint, vsnr, ag),
                    HttpMethod.POST, new HttpEntity<>(mitarbeiter, getHeaders()), String.class);
        } catch (HttpStatusCodeException e) {
            status = "ok";
        }
        return status;
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

    private String getUrl(String endpoint, String vsnr, String ag) {
        return UriComponentsBuilder.fromUriString(endpoint) //
                .queryParam("ag", ag) //
                .buildAndExpand(vsnr) //
                .encode(StandardCharsets.UTF_8) //
                .toUriString();
    }

}
