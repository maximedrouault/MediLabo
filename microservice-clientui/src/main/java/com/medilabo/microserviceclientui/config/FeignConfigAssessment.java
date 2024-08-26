package com.medilabo.microserviceclientui.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfigAssessment {

    @Value("${feign.client.assessment.username}")
    private String username;

    @Value("${feign.client.assessment.password}")
    private String password;


    @Bean
    public BasicAuthRequestInterceptor assessmentAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }
}