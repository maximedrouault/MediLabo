package com.medilabo.microserviceassessment.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfigNote {

    @Value("${feign.client.note.username}")
    private String username;

    @Value("${feign.client.note.password}")
    private String password;


    @Bean
    public BasicAuthRequestInterceptor noteAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }
}