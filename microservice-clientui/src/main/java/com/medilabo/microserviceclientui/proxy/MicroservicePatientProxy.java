package com.medilabo.microserviceclientui.proxy;

import com.medilabo.microserviceclientui.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "GATEWAY-SERVER", contextId = "patientProxy")
public interface MicroservicePatientProxy {

    @GetMapping("/MICROSERVICE-PATIENT/patient/list")
    List<PatientDTO> getAllPatients();

    @GetMapping("/MICROSERVICE-PATIENT/patient/{id}")
    PatientDTO getPatient(@PathVariable Long id);

    @DeleteMapping("/MICROSERVICE-PATIENT/patient/delete/{id}")
    void deletePatientById(@PathVariable Long id);

    @PostMapping("/MICROSERVICE-PATIENT/patient/add")
    void savePatient(@RequestBody PatientDTO patient);
}
