package com.medilabo.microserviceassessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroserviceAssessmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceAssessmentApplication.class, args);
    }

}
