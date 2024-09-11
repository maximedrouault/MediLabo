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
        // Obtention du token CSRF et du cookie de session
        ResponseEntity<String> loginPageResponse = restTemplate.getForEntity("http://localhost:" + port + "/login", String.class);
        String csrfToken = extractCsrfToken(Objects.requireNonNull(loginPageResponse.getBody()));
        String sessionCookie = loginPageResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        // Construction de la requête de login
        String formBody = "username=clientui_user_test&password=clientui_password_test&_csrf=" + csrfToken;
        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        loginHeaders.add(HttpHeaders.COOKIE, sessionCookie);
        HttpEntity<String> loginRequest = new HttpEntity<>(formBody, loginHeaders);

        // Envoi de la requête de login
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("http://localhost:" + port + "/login", loginRequest, String.class);
        return loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }

    public String extractCsrfToken(String body) {
        // Extraction du token CSRF depuis le corps de la page
        String tokenPrefix = "name=\"_csrf\" value=\"";
        int startIndex = body.indexOf(tokenPrefix) + tokenPrefix.length();
        int endIndex = body.indexOf("\"", startIndex);
        return body.substring(startIndex, endIndex);
    }
}
