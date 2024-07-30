package com.medilabo.microservicepatient.repository;


import com.medilabo.microservicepatient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
