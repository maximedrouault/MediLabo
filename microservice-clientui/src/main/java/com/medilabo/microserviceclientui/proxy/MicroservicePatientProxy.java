package com.medilabo.microserviceclientui.proxy;

import com.medilabo.microserviceclientui.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "microservice-patient", url = "${patient-api.url}")
public interface MicroservicePatientProxy {

    @GetMapping(value = "/patients")
    List<PatientDTO> getPatients();

    @GetMapping(value = "/patient/{id}")
    PatientDTO getPatient(@PathVariable Long id);
}
