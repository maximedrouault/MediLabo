package com.medilabo.mpatient.repository;


import com.medilabo.mpatient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
