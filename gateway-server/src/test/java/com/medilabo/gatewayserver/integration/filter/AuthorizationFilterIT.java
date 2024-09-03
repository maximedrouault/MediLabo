package com.medilabo.gatewayserver.integration.filter;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@WireMockTest(httpPort = 9999)
class AuthorizationFilterIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;


    // Health check endpoint access tests
    @Test
    void allowsUnauthenticatedHealthCheckRequests() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/MICROSERVICE-PATIENT/actuator/health", String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"status\":\"UP\""));
    }

    // Protected microservice endpoints access tests
    @Test
    void rejectsRequestsWithMissingAuthorizationHeader() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/MICROSERVICE-PATIENT/patient/list", String.class);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
    }

    @Test
    void rejectsRequestsWithInvalidAuthorizationHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "InvalidAuthHeader");

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/MICROSERVICE-PATIENT/patient/list", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
    }

    @Test
    void allowsRequestsWithValidAuthorizationHeader() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("patient_user_test", "patient_password_test")
                .getForEntity("http://localhost:" + port + "/MICROSERVICE-PATIENT/patient/list", String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"id\":1"));
        assertTrue(response.getBody().contains("\"firstName\":\"John\""));
        assertTrue(response.getBody().contains("\"lastName\":\"Doe\""));
        assertTrue(response.getBody().contains("\"dateOfBirth\":\"1980-01-01\""));
        assertTrue(response.getBody().contains("\"gender\":\"M\""));
        assertTrue(response.getBody().contains("\"address\":\"123 Main St.\""));
        assertTrue(response.getBody().contains("\"telephoneNumber\":\"123-456-7890\""));
    }
}