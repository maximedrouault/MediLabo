package com.medilabo.microserviceclientui.integration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@RequiredArgsConstructor
public class AuthHelper {

    private final TestRestTemplate restTemplate;


    public String authenticateToFront(int port) {
        // Obtaining the CSRF token and session cookie
        ResponseEntity<String> loginPageResponse = restTemplate.getForEntity("http://localhost:" + port + "/login", String.class);
        String csrfToken = extractCsrfToken(Objects.requireNonNull(loginPageResponse.getBody()));
        String sessionCookie = loginPageResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        // Construction of the login request
        String formBody = "username=clientui_user_test&password=clientui_password_test&_csrf=" + csrfToken;
        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        loginHeaders.add(HttpHeaders.COOKIE, sessionCookie);
        HttpEntity<String> loginRequest = new HttpEntity<>(formBody, loginHeaders);

        // Sending the login request
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("http://localhost:" + port + "/login", loginRequest, String.class);
        return loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }

    public String extractCsrfToken(String body) {
        // Extracting the CSRF token from the page body
        String tokenPrefix = "name=\"_csrf\" value=\"";
        int startIndex = body.indexOf(tokenPrefix) + tokenPrefix.length();
        int endIndex = body.indexOf("\"", startIndex);
        return body.substring(startIndex, endIndex);
    }
}
