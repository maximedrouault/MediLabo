package com.medilabo.microservicepatient.controller;

import com.medilabo.microservicepatient.model.Patient;
import com.medilabo.microservicepatient.repository.PatientRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final PatientRepository patientRepository;


    @GetMapping("/list")
    @Operation(summary = "Get all patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientRepository.findAll(Sort.by(Sort.Direction.ASC, "lastName"));

        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a patient by id", parameters = {
            @Parameter(name = "id", description = "ID of the patient to be retrieved", required = true, example = "1"),
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);

        return patientOptional
                .map(patientFound -> new ResponseEntity<>(patientFound, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    @Operation(summary = "Add a patient",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Patient details to be added", required = true),
            responses = {
            @ApiResponse(responseCode = "201", description = "Patient added")
    })
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        Patient patientCreated = patientRepository.save(patient);

        return new ResponseEntity<>(patientCreated, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @Operation(summary = "Update a patient",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Patient details to be updated", required = true),
            responses = {
            @ApiResponse(responseCode = "200", description = "Patient updated"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Patient> updatePatient(@RequestBody Patient patientDetails) {
        Optional<Patient> patientOptional = patientRepository.findById(patientDetails.getId());

        if (patientOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Patient existingPatient = patientOptional.get();

            existingPatient.setLastName(patientDetails.getLastName());
            existingPatient.setFirstName(patientDetails.getFirstName());
            existingPatient.setDateOfBirth(patientDetails.getDateOfBirth());
            existingPatient.setGender(patientDetails.getGender());
            existingPatient.setAddress(patientDetails.getAddress());
            existingPatient.setTelephoneNumber(patientDetails.getTelephoneNumber());

            Patient patientUpdated = patientRepository.save(existingPatient);

            return new ResponseEntity<>(patientUpdated, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a patient", parameters = {
            @Parameter(name = "id", description = "ID of the patient to be deleted", required = true, example = "1"),
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Patient deleted"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);


        if (patientOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {

            patientRepository.deleteById(id);

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
