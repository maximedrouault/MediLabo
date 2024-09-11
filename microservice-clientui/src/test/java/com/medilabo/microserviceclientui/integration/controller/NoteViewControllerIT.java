package com.medilabo.microserviceclientui.integration.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
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
@WireMockTest(httpPort = 9999)
class NoteViewControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthHelper authHelper;

    @LocalServerPort
    private int port;


    // Note list tests
    @Test
    void noteListReturnsAllNotesWhenAuthenticated() {
        String authCookie = authHelper.authenticateToFront(port);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        ResponseEntity<String> noteListResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/list/1", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertTrue(noteListResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(noteListResponse.getBody());
        String responseBody = noteListResponse.getBody();
        assertTrue(responseBody.contains("John"));
        assertTrue(responseBody.contains("Doe"));
        assertTrue(responseBody.contains("2021-02-01"));
        assertTrue(responseBody.contains("08:13"));
        assertTrue(responseBody.contains("Total number of notes : 1"));
        assertTrue(responseBody.contains("Risk level : None"));
        assertTrue(responseBody.contains("This is a test note"));
    }

    @Test
    void noteListRedirectsToLoginPageWhenNotAuthenticated() {
        ResponseEntity<String> noteListResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/note/list/1", String.class);

        assertTrue(noteListResponse.getStatusCode().is3xxRedirection());
        assertEquals("/login", Objects.requireNonNull(Objects.requireNonNull(noteListResponse.getHeaders().getLocation()).getPath()));
        assertNull(noteListResponse.getBody());
    }


    // Note details tests
    @Test
    void noteDetailsReturnsNoteWhenAuthenticated() {
        String authCookie = authHelper.authenticateToFront(port);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        ResponseEntity<String> noteDetailsResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/66d583a8402cc16f3d5e739c", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertTrue(noteDetailsResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(noteDetailsResponse.getBody());
        String responseBody = noteDetailsResponse.getBody();
        assertTrue(responseBody.contains("John"));
        assertTrue(responseBody.contains("2021-02-01"));
        assertTrue(responseBody.contains("08:13"));
        assertTrue(responseBody.contains("This is a test note"));
    }

    @Test
    void noteDetailsRedirectsToLoginPageWhenNotAuthenticated() {
        ResponseEntity<String> noteDetailsResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/note/66d583a8402cc16f3d5e739c", String.class);

        assertTrue(noteDetailsResponse.getStatusCode().is3xxRedirection());
        assertEquals("/login", Objects.requireNonNull(Objects.requireNonNull(noteDetailsResponse.getHeaders().getLocation()).getPath()));
        assertNull(noteDetailsResponse.getBody());
    }


    // Note delete tests
    @Test
    void deleteNoteRedirectsToLoginPageWhenNotAuthenticated() {
        ResponseEntity<String> deleteNoteResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/note/delete/66d583a8402cc16f3d5e739c", String.class);

        assertTrue(deleteNoteResponse.getStatusCode().is3xxRedirection());
        assertEquals("/login", Objects.requireNonNull(Objects.requireNonNull(deleteNoteResponse.getHeaders().getLocation()).getPath()));
        assertNull(deleteNoteResponse.getBody());
    }

    @Test
    void deleteNoteRedirectsToNoteListWhenAuthenticatedAndDeleteNoteIsOk() {
        String authCookie = authHelper.authenticateToFront(port);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        ResponseEntity<String> deleteNoteResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/delete/66d583a8402cc16f3d5e739c", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertTrue(deleteNoteResponse.getStatusCode().is3xxRedirection());
        assertEquals("/note/list/1", Objects.requireNonNull(deleteNoteResponse.getHeaders().getLocation()).getPath());
        assertNull(deleteNoteResponse.getBody());
    }


    // Note add tests
    @Test
    void addNoteFormReturnsAddNoteFormWhenAuthenticated() {
        String authCookie = authHelper.authenticateToFront(port);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        ResponseEntity<String> addNoteFormResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/add/1", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertTrue(addNoteFormResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(addNoteFormResponse.getBody());
        String responseBody = addNoteFormResponse.getBody();
        assertTrue(responseBody.contains("Doe"));
        assertTrue(responseBody.contains("Note content"));
    }

    @Test
    void addNoteFormRedirectsToLoginPageWhenNotAuthenticated() {
        ResponseEntity<String> addNoteFormResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/note/add/1", String.class);

        assertTrue(addNoteFormResponse.getStatusCode().is3xxRedirection());
        assertEquals("/login", Objects.requireNonNull(Objects.requireNonNull(addNoteFormResponse.getHeaders().getLocation()).getPath()));
        assertNull(addNoteFormResponse.getBody());
    }


    // Note save tests
    @Test
    void saveNoteRedirectsToLoginPageWhenNotAuthenticated() {
        ResponseEntity<String> saveNoteResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/note/save", null, String.class);

        assertTrue(saveNoteResponse.getStatusCode().is3xxRedirection());
        assertTrue(Objects.requireNonNull(saveNoteResponse.getHeaders().getLocation()).getPath().contains("/login"));
        assertNull(saveNoteResponse.getBody());
    }

    @Test
    void saveNoteRedirectsToNoteListWhenAuthenticatedAndSaveNoteIsOk() {
        String authCookie = authHelper.authenticateToFront(port);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        // Obtain the CSRF token from the add note form
        ResponseEntity<String> addNoteFormResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/add/1", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        String csrfToken = authHelper.extractCsrfToken(Objects.requireNonNull(addNoteFormResponse.getBody()));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String formBody = "patientId=1&patientName=John&noteContent=This is a test note added&_csrf=" + csrfToken;

        ResponseEntity<String> saveNoteResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/save", HttpMethod.POST, new HttpEntity<>(formBody, headers), String.class);

        assertTrue(saveNoteResponse.getStatusCode().is3xxRedirection());
        assertEquals("/note/list/1", Objects.requireNonNull(saveNoteResponse.getHeaders().getLocation()).getPath());
        assertNull(saveNoteResponse.getBody());
    }

    @Test
    void saveNoteReturnsAddNoteFormWhenAuthenticatedAndFormIsInvalid() {
        String authCookie = authHelper.authenticateToFront(port);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        // Obtain the CSRF token from the add note form
        ResponseEntity<String> addNoteFormResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/add/1", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        String csrfToken = authHelper.extractCsrfToken(Objects.requireNonNull(addNoteFormResponse.getBody()));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String formBody = "patientId=1&patientName=John&noteContent=&_csrf=" + csrfToken;

        ResponseEntity<String> saveNoteResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/save", HttpMethod.POST, new HttpEntity<>(formBody, headers), String.class);

        assertTrue(saveNoteResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(saveNoteResponse.getBody());
        String responseBody = saveNoteResponse.getBody();
        assertTrue(responseBody.contains("John"));
        assertTrue(responseBody.contains("Note content"));
        assertTrue(responseBody.contains("must not be blank"));
    }


    // Note update tests
    @Test
    void updateNoteFormReturnsUpdateNoteFormWhenAuthenticated() {
        String authCookie = authHelper.authenticateToFront(port);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        ResponseEntity<String> updateNoteFormResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/update/66d583a8402cc16f3d5e739c", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertTrue(updateNoteFormResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(updateNoteFormResponse.getBody());
        String responseBody = updateNoteFormResponse.getBody();
        assertTrue(responseBody.contains("John"));
        assertTrue(responseBody.contains("This is a test note"));
    }

    @Test
    void updateNoteFormRedirectsToLoginPageWhenNotAuthenticated() {
        ResponseEntity<String> updateNoteFormResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/note/update/66d583a8402cc16f3d5e739c", String.class);

        assertTrue(updateNoteFormResponse.getStatusCode().is3xxRedirection());
        assertEquals("/login", Objects.requireNonNull(Objects.requireNonNull(updateNoteFormResponse.getHeaders().getLocation()).getPath()));
        assertNull(updateNoteFormResponse.getBody());
    }

    @Test
    void updateNoteRedirectsToNoteListWhenAuthenticatedAndUpdateNoteIsOk() {
        String authCookie = authHelper.authenticateToFront(port);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        // Obtain the CSRF token from the update note form
        ResponseEntity<String> updateNoteFormResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/update/66d583a8402cc16f3d5e739c", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        String csrfToken = authHelper.extractCsrfToken(Objects.requireNonNull(updateNoteFormResponse.getBody()));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String formBody = "id=66d583a8402cc16f3d5e739c&patientId=1&patientName=John&noteContent=This is a test note updated&_csrf=" + csrfToken;

        ResponseEntity<String> updateNoteResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/update", HttpMethod.POST, new HttpEntity<>(formBody, headers), String.class);

        assertTrue(updateNoteResponse.getStatusCode().is3xxRedirection());
        assertEquals("/note/list/1", Objects.requireNonNull(updateNoteResponse.getHeaders().getLocation()).getPath());
        assertNull(updateNoteResponse.getBody());
    }

    @Test
    void updateNoteReturnsUpdateNoteFormWhenAuthenticatedAndFormIsInvalid() {
        String authCookie = authHelper.authenticateToFront(port);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, authCookie);

        // Obtain the CSRF token from the update note form
        ResponseEntity<String> updateNoteFormResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/update/66d583a8402cc16f3d5e739c", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        String csrfToken = authHelper.extractCsrfToken(Objects.requireNonNull(updateNoteFormResponse.getBody()));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String formBody = "id=66d583a8402cc16f3d5e739c&patientId=1&patientName=John&noteContent=&_csrf=" + csrfToken;

        ResponseEntity<String> updateNoteResponse = restTemplate.exchange(
                "http://localhost:" + port + "/note/update", HttpMethod.POST, new HttpEntity<>(formBody, headers), String.class);

        assertTrue(updateNoteResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(updateNoteResponse.getBody());
        String responseBody = updateNoteResponse.getBody();
        assertTrue(responseBody.contains("John"));
        assertTrue(responseBody.contains("Note content"));
        assertTrue(responseBody.contains("must not be blank"));
    }
}
