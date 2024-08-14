package com.medilabo.microservicepatient.controller;

import com.medilabo.microservicepatient.model.Patient;
import com.medilabo.microservicepatient.repository.PatientRepository;
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
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientRepository.findAll(Sort.by(Sort.Direction.ASC, "lastName"));

        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);

        return patientOptional
                .map(patientFound -> new ResponseEntity<>(patientFound, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        Patient patientCreated = patientRepository.save(patient);

        return new ResponseEntity<>(patientCreated, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
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

    @DeleteMapping("/delete/{id}")
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
