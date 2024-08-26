package com.medilabo.microservicepatient.unit.controller;

import com.medilabo.microservicepatient.controller.PatientController;
import com.medilabo.microservicepatient.model.Patient;
import com.medilabo.microservicepatient.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientController patientController;


    // getAllPatients
    @Test
    void getAllPatientsReturnsListOfPatients() {
        List<Patient> patients = List.of(Patient.builder().build());
        when(patientRepository.findAll(Sort.by(Sort.Direction.ASC, "lastName"))).thenReturn(patients);

        ResponseEntity<List<Patient>> response = patientController.getAllPatients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void getAllPatientsReturnsEmptyListWhenNoPatients() {
        when(patientRepository.findAll(Sort.by(Sort.Direction.ASC, "lastName"))).thenReturn(List.of());

        ResponseEntity<List<Patient>> response = patientController.getAllPatients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, Objects.requireNonNull(response.getBody()).size());
    }

    // getPatient
    @Test
    void getPatientReturnsPatientWhenIdExists() {
        Patient patient = Patient.builder().id(1L).build();
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        ResponseEntity<Patient> response = patientController.getPatient(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
    }

    @Test
    void getPatientReturnsNotFoundWhenIdDoesNotExist() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Patient> response = patientController.getPatient(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // addPatient
    @Test
    void addPatientReturnsCreatedStatusAndPatient() {
        Patient patient = Patient.builder().id(1L).build();
        when(patientRepository.save(patient)).thenReturn(patient);

        ResponseEntity<Patient> response = patientController.addPatient(patient);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(patient, response.getBody());
    }

    // updatePatient
    @Test
    void updatePatientReturnsUpdatedPatientWhenIdExists() {
        Patient existingPatient = Patient.builder().id(1L).build();
        Patient updatedDetails = Patient.builder().id(1L).lastName("Doe").build();
        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(existingPatient)).thenReturn(updatedDetails);

        ResponseEntity<Patient> response = patientController.updatePatient(updatedDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDetails, response.getBody());
    }

    @Test
    void updatePatientReturnsNotFoundWhenIdDoesNotExist() {
        Patient updatedDetails = Patient.builder().id(1L).build();
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Patient> response = patientController.updatePatient(updatedDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // deletePatient
    @Test
    void deletePatientReturnsOkWhenIdExists() {
        Patient patient = Patient.builder().id(1L).build();
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        ResponseEntity<Void> response = patientController.deletePatient(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deletePatientReturnsNotFoundWhenIdDoesNotExist() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = patientController.deletePatient(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
