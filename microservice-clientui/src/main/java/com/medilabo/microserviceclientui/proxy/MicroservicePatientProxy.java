package com.medilabo.microserviceclientui.proxy;

import com.medilabo.microserviceclientui.config.FeignConfigPatient;
import com.medilabo.microserviceclientui.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "GATEWAY-SERVER", contextId = "patientProxy", configuration = FeignConfigPatient.class)
public interface MicroservicePatientProxy {

    @GetMapping("/MICROSERVICE-PATIENT/patient/list")
    List<PatientDTO> getAllPatients();

    @GetMapping("/MICROSERVICE-PATIENT/patient/{id}")
    PatientDTO getPatient(@PathVariable Long id);

    @DeleteMapping("/MICROSERVICE-PATIENT/patient/delete/{id}")
    void deletePatient(@PathVariable Long id);

    @PostMapping("/MICROSERVICE-PATIENT/patient/add")
    PatientDTO savePatient(@RequestBody PatientDTO patient);

    @PutMapping("/MICROSERVICE-PATIENT/patient/update")
    PatientDTO updatePatient(@RequestBody PatientDTO patient);
}
