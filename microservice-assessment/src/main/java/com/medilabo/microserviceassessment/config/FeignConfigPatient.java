package com.medilabo.microserviceassessment.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfigPatient {

    @Value("${feign.client.patient.username}")
    private String username;

    @Value("${feign.client.patient.password}")
    private String password;


    @Bean
    public BasicAuthRequestInterceptor patientAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }
}