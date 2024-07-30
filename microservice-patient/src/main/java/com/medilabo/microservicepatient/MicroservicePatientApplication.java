package com.medilabo.microservicepatient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroservicePatientApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicePatientApplication.class, args);
    }

}
