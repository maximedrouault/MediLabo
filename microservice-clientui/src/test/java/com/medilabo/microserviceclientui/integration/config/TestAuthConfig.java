package com.medilabo.microserviceclientui.integration.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestAuthConfig {

    @Bean
    public AuthHelper authHelper(TestRestTemplate restTemplate) {
        return new AuthHelper(restTemplate);
    }
}