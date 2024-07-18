package com.medilabo.mpatient.controller;

import com.medilabo.mpatient.model.Patient;
import com.medilabo.mpatient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PatientController {

    private final PatientRepository patientRepository;


    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return new ResponseEntity<>(patientRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);

        return patientOptional
                .map(patientFound -> new ResponseEntity<>(patientFound, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/patient")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient patientCreated = patientRepository.save(patient);

        return new ResponseEntity<>(patientCreated, HttpStatus.CREATED);
    }

    @PutMapping("/patient/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patientDetails) {
        Optional<Patient> patientOptional = patientRepository.findById(id);

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
}
