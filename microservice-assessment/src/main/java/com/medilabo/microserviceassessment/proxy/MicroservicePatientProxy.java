package com.medilabo.microserviceassessment.proxy;

import com.medilabo.microserviceassessment.config.FeignConfigPatient;
import com.medilabo.microserviceassessment.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "GATEWAY-SERVER", contextId = "patientProxy", configuration = FeignConfigPatient.class)
public interface MicroservicePatientProxy {

    @GetMapping("/MICROSERVICE-PATIENT/patient/{id}")
    PatientDTO getPatient(@PathVariable Long id);
}