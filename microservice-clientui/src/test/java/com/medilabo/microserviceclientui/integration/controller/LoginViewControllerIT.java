package com.medilabo.microserviceclientui.integration.controller;

import com.medilabo.microserviceclientui.integration.config.AuthHelper;
import com.medilabo.microserviceclientui.integration.config.TestAuthConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestAuthConfig.class)
class LoginViewControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthHelper authHelper;

    @LocalServerPort
    private int port;


    @Test
    void homeRedirectsToLoginWhenNotAuthenticated() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/", String.class);

        assertTrue(response.getStatusCode().is3xxRedirection());
        assertEquals("/login", Objects.requireNonNull(response.getHeaders().getLocation()).getPath());
        assertNull(response.getBody());
    }

    @Test
    void homeRedirectsToPatientListWhenAuthenticated() {
        String authCookie = authHelper.authenticateToFront(port);
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
        String csrfToken = authHelper.extractCsrfToken(Objects.requireNonNull(loginPageResponse.getBody()));
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
        String authCookie = loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        assertTrue(loginResponse.getStatusCode().is3xxRedirection());
        assertEquals("/patient/list", Objects.requireNonNull(loginResponse.getHeaders().getLocation()).getPath());
        assertNull(loginResponse.getBody());
        assertNotNull(authCookie);
    }
}
