package com.medilabo.microserviceclientui.integration.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@WireMockTest(httpPort = 9999)
public class LoginViewControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String authCookie;


    private void authenticateToFront() {
        // Step 1 : Obtain the CSRF token and session cookie from the login page
        ResponseEntity<String> loginPageResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/login", String.class);
        String csrfToken = extractCsrfToken(Objects.requireNonNull(loginPageResponse.getBody()));
        String sessionCookie = loginPageResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        // Step 2 : Construct the login request with auth credentials, CSRF token and session cookie in FORM_URLENCODED format
        String formBody = "username=clientui_user_test&password=clientui_password_test&_csrf=" + csrfToken;
        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        loginHeaders.add(HttpHeaders.COOKIE, sessionCookie); // Add the session cookie
        HttpEntity<String> loginRequest = new HttpEntity<>(formBody, loginHeaders);

        // Send the login request and get the auth session cookie from the response
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/login", loginRequest, String.class);
        authCookie = loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }

    private String extractCsrfToken(String body) {
        // Extract CSRF token from the page body
        String tokenPrefix = "name=\"_csrf\" value=\"";
        int startIndex = body.indexOf(tokenPrefix) + tokenPrefix.length();
        int endIndex = body.indexOf("\"", startIndex);
        return body.substring(startIndex, endIndex);
    }


    @Test
    void homeRedirectsToLoginWhenNotAuthenticated() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/", String.class);

        assertTrue(response.getStatusCode().is3xxRedirection());
        assertEquals("/login", Objects.requireNonNull(response.getHeaders().getLocation()).getPath());
        assertNull(response.getBody());
    }

    @Test
    void homeRedirectsToPatientListWhenAuthenticated() {
        authenticateToFront();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertTrue(response.getStatusCode().is3xxRedirection());
        assertEquals("/patient/list", Objects.requireNonNull(response.getHeaders().getLocation()).getPath());
        assertNull(response.getBody());
    }

    @Test
    void loginReturnsLoginView() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/login", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        String responseBody = response.getBody();
        assertTrue(responseBody.contains("username"));
        assertTrue(responseBody.contains("password"));
    }

    @Test
    void loginReturnsPatientListWhenAuthenticationIsOK() {
        ResponseEntity<String> loginPageResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/login", String.class);
        String csrfToken = extractCsrfToken(Objects.requireNonNull(loginPageResponse.getBody()));
        String sessionCookie = loginPageResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        // Step 2 : Construct the login request with auth credentials, CSRF token and session cookie in FORM_URLENCODED format
        String formBody = "username=clientui_user_test&password=clientui_password_test&_csrf=" + csrfToken;
        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        loginHeaders.add(HttpHeaders.COOKIE, sessionCookie); // Add the session cookie
        HttpEntity<String> loginRequest = new HttpEntity<>(formBody, loginHeaders);

        // Send the login request and get the auth session cookie from the response
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/login", loginRequest, String.class);
        authCookie = loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        assertTrue(loginResponse.getStatusCode().is3xxRedirection());
        assertEquals("/patient/list", Objects.requireNonNull(loginResponse.getHeaders().getLocation()).getPath());
        assertNull(loginResponse.getBody());
        assertNotNull(authCookie);
    }
}
