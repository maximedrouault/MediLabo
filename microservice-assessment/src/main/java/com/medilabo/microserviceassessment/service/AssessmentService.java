package com.medilabo.microserviceassessment.service;

import com.medilabo.microserviceassessment.dto.PatientDTO;
import com.medilabo.microserviceassessment.model.RiskLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
@Log4j2
public class AssessmentService {

    private static final int AGE_THRESHOLD = 30;

    public RiskLevel getAssessment(PatientDTO patientDTO, Integer riskTermsInPatientNotes) {
        int patientDTOAge = Period.between(patientDTO.getDateOfBirth(), LocalDate.now()).getYears();
        PatientDTO.Gender patientDTOGender = patientDTO.getGender();

        // Risk level : None
        if (riskTermsInPatientNotes < 2) {
            return RiskLevel.NONE;
        }

        // Risk level : Borderline
        if (riskTermsInPatientNotes <= 5 && patientDTOAge >= AGE_THRESHOLD) {
            return RiskLevel.BORDERLINE;
        }

        // Risk level : In Danger
        if (patientDTOGender == PatientDTO.Gender.M && patientDTOAge < AGE_THRESHOLD && riskTermsInPatientNotes == 3 ||
            patientDTOGender == PatientDTO.Gender.F && patientDTOAge < AGE_THRESHOLD && riskTermsInPatientNotes == 4 ||
            patientDTOAge >= AGE_THRESHOLD && riskTermsInPatientNotes <= 7) {

            return RiskLevel.IN_DANGER;
        }

        // Risk level : Early onset
        if (patientDTOGender == PatientDTO.Gender.M && patientDTOAge < AGE_THRESHOLD && riskTermsInPatientNotes >= 5 ||
            patientDTOGender == PatientDTO.Gender.F && patientDTOAge < AGE_THRESHOLD && riskTermsInPatientNotes >= 7 ||
            patientDTOAge >= AGE_THRESHOLD) {

            return RiskLevel.EARLY_ONSET;
        }

        return RiskLevel.UNKNOWN_RISK_LEVEL;
    }
}