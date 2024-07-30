package com.medilabo.microserviceclientui.proxy;

import com.medilabo.microserviceclientui.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "microservice-patient", url = "${patient-api.url}")
public interface MicroservicePatientProxy {

    @GetMapping("/patient/list")
    List<PatientDTO> getAllPatients();

    @GetMapping("/patient/{id}")
    PatientDTO getPatient(@PathVariable Long id);

    @DeleteMapping("/patient/delete/{id}")
    void deletePatient(@PathVariable Long id);

    @PostMapping("/patient/add")
    void savePatient(@RequestBody PatientDTO patient);
}
