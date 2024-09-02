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
public class PatientViewControllerIT {

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


    // Patient list tests
    @Test
    void patientListReturnsAllPatientsWhenAuthenticated() {
        authenticateToFront();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        ResponseEntity<String> patientListResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/list", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.OK, patientListResponse.getStatusCode());
        assertNotNull(patientListResponse.getBody());
        assertTrue((patientListResponse.getBody()).contains("John"));
        assertTrue((patientListResponse.getBody()).contains("Doe"));
        assertTrue((patientListResponse.getBody()).contains("1980-01-01"));
        assertTrue((patientListResponse.getBody()).contains("M"));
        assertTrue((patientListResponse.getBody()).contains("123 Main St"));
        assertTrue((patientListResponse.getBody()).contains("123-456-7890"));
    }

    @Test
    void patientListReturnsRedirectToLoginPageWhenNotAuthenticated() {
        ResponseEntity<String> patientListResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/patient/list", String.class);

        assertTrue(patientListResponse.getStatusCode().is3xxRedirection());
        assertEquals("/login", Objects.requireNonNull(patientListResponse.getHeaders().getLocation()).getPath());
        assertNull(patientListResponse.getBody());
    }


    // Patient delete tests
    @Test
    void deletePatientRedirectsToPatientListWhenAuthenticatedAndDeleteNotesAndPatientIsOk() {
        authenticateToFront();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        ResponseEntity<String> deletePatientResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/delete/1", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertTrue(deletePatientResponse.getStatusCode().is3xxRedirection());
        assertEquals("/patient/list", Objects.requireNonNull(deletePatientResponse.getHeaders().getLocation()).getPath());
        assertNull(deletePatientResponse.getBody());
    }

    @Test
    void deletePatientRedirectsToLoginPageWhenNotAuthenticated() {
        ResponseEntity<String> deletePatientResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/patient/delete/1", String.class);

        assertTrue(deletePatientResponse.getStatusCode().is3xxRedirection());
        assertEquals("/login", Objects.requireNonNull(deletePatientResponse.getHeaders().getLocation()).getPath());
        assertNull(deletePatientResponse.getBody());
    }


    // Add patient tests
    @Test
    void addPatientRedirectsToLoginPageWhenNotAuthenticated() {
        ResponseEntity<String> addPatientResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/patient/add", String.class);

        assertTrue(addPatientResponse.getStatusCode().is3xxRedirection());
        assertTrue(Objects.requireNonNull(addPatientResponse.getHeaders().getLocation()).getPath().contains("/login"));
        assertNull(addPatientResponse.getBody());
    }

    @Test
    void addPatientReturnsAddPatientFormWhenAuthenticated() {
        authenticateToFront();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        ResponseEntity<String> addPatientResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/add", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertTrue(addPatientResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(addPatientResponse.getBody());
        String responseBody = addPatientResponse.getBody();
        assertTrue(responseBody.contains("Lastname"));
        assertTrue(responseBody.contains("Firstname"));
        assertTrue(responseBody.contains("Date of birth"));
        assertTrue(responseBody.contains("gender"));
        assertTrue(responseBody.contains("Address"));
        assertTrue(responseBody.contains("Telephone number"));
    }


    // Patient save tests
    @Test
    void savePatientRedirectsToPatientListWhenAuthenticatedAndAddIsOk() {
        authenticateToFront();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        // Obtain the CSRF token from the add patient form
        ResponseEntity<String> addPatientFormResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/add", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        String csrfToken = extractCsrfToken(Objects.requireNonNull(addPatientFormResponse.getBody()));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String formBody = "lastName=Doe&firstName=Jane&dateOfBirth=1980-01-01&gender=F&address=123 Main St&telephoneNumber=123-456-7890&_csrf=" + csrfToken;

        // Send the add patient request
        ResponseEntity<String> savePatientResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/save", HttpMethod.POST, new HttpEntity<>(formBody, headers), String.class);

        assertTrue(savePatientResponse.getStatusCode().is3xxRedirection());
        assertEquals("/patient/list", Objects.requireNonNull(savePatientResponse.getHeaders().getLocation()).getPath());
        assertNull(savePatientResponse.getBody());
    }

    @Test
    void savePatientRedirectsToLoginPageWhenNotAuthenticated() {
        String formBody = "lastName=Doe&firstName=Jane&dateOfBirth=1980-01-01&gender=F&address=123 Main St&telephoneNumber=123-456-7890";

        ResponseEntity<String> savePatientResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/save", HttpMethod.POST, new HttpEntity<>(formBody, new HttpHeaders()), String.class);

        assertTrue(savePatientResponse.getStatusCode().is3xxRedirection());
        assertTrue(Objects.requireNonNull(savePatientResponse.getHeaders().getLocation()).getPath().contains("/login"));
        assertNull(savePatientResponse.getBody());
    }

    @Test
    void savePatientReturnsAddPatientFormWhenAuthenticatedAndFormIsInvalid() {
        authenticateToFront();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        // Obtain the CSRF token from the add patient form
        ResponseEntity<String> addPatientFormResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/add", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        String csrfToken = extractCsrfToken(Objects.requireNonNull(addPatientFormResponse.getBody()));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String formBody = "lastName=&firstName=&dateOfBirth=&gender=&address=&telephoneNumber=&_csrf=" + csrfToken;

        // Send the add patient request with invalid form data
        ResponseEntity<String> savePatientResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/save", HttpMethod.POST, new HttpEntity<>(formBody, headers), String.class);

        assertTrue(savePatientResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(savePatientResponse.getBody());
        String responseBody = savePatientResponse.getBody();
        assertTrue(responseBody.contains("Lastname"));
        assertTrue(responseBody.contains("Firstname"));
        assertTrue(responseBody.contains("Date of birth"));
        assertTrue(responseBody.contains("gender"));
        assertTrue(responseBody.contains("Address"));
        assertTrue(responseBody.contains("Telephone number"));
    }


    // Patient update tests
    @Test
    void updatePatientReturnsUpdatePatientFormWhenAuthenticated() {
        authenticateToFront();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        ResponseEntity<String> updatePatientFormResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/update/1", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertTrue(updatePatientFormResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(updatePatientFormResponse.getBody());
        String responseBody = updatePatientFormResponse.getBody();
        assertTrue(responseBody.contains("John"));
        assertTrue(responseBody.contains("Doe"));
        assertTrue(responseBody.contains("1980-01-01"));
        assertTrue(responseBody.contains("M"));
        assertTrue(responseBody.contains("123 Main St"));
        assertTrue(responseBody.contains("123-456-7890"));
    }

    @Test
    void updatePatientRedirectsToLoginPageWhenNotAuthenticated() {
        ResponseEntity<String> updatePatientFormResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/patient/update/1", String.class);

        assertTrue(updatePatientFormResponse.getStatusCode().is3xxRedirection());
        assertEquals("/login", Objects.requireNonNull(updatePatientFormResponse.getHeaders().getLocation()).getPath());
        assertNull(updatePatientFormResponse.getBody());
    }

    @Test
    void updatePatientRedirectsToPatientListWhenAuthenticatedAndUpdateIsOk() {
        authenticateToFront();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        // Obtain the CSRF token from the update patient form
        ResponseEntity<String> updatePatientFormResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/update/1", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        String csrfToken = extractCsrfToken(Objects.requireNonNull(updatePatientFormResponse.getBody()));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String formBody = "id=1&lastName=Doe&firstName=Jane&dateOfBirth=1980-01-01&gender=F&address=123 Main St&telephoneNumber=123-456-7890&_csrf=" + csrfToken;

        // Send the update patient request
        ResponseEntity<String> updatePatientResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/update", HttpMethod.POST, new HttpEntity<>(formBody, headers), String.class);

        assertTrue(updatePatientResponse.getStatusCode().is3xxRedirection());
        assertEquals("/patient/list", Objects.requireNonNull(updatePatientResponse.getHeaders().getLocation()).getPath());
        assertNull(updatePatientResponse.getBody());
    }

    @Test
    void updatePatientReturnsUpdatePatientFormWhenAuthenticatedAndFormIsInvalid() {
        authenticateToFront();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        // Obtain the CSRF token from the update patient form
        ResponseEntity<String> updatePatientFormResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/update/1", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        String csrfToken = extractCsrfToken(Objects.requireNonNull(updatePatientFormResponse.getBody()));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String formBody = "id=1&lastName=&firstName=&dateOfBirth=&gender=&address=&telephoneNumber=&_csrf=" + csrfToken;

        // Send the update patient request with invalid form data
        ResponseEntity<String> updatePatientResponse = restTemplate.exchange(
                "http://localhost:" + port + "/patient/update", HttpMethod.POST, new HttpEntity<>(formBody, headers), String.class);

        assertTrue(updatePatientResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(updatePatientResponse.getBody());
        String responseBody = updatePatientResponse.getBody();
        assertTrue(responseBody.contains("Lastname"));
        assertTrue(responseBody.contains("Firstname"));
        assertTrue(responseBody.contains("Date of birth"));
        assertTrue(responseBody.contains("gender"));
        assertTrue(responseBody.contains("Address"));
        assertTrue(responseBody.contains("Telephone number"));
    }
}