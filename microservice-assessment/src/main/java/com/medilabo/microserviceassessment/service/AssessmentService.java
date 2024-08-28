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

    public RiskLevel getAssessment(PatientDTO patient, Integer riskTermsFound) {
        int patientAge = Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();
        boolean isMale = patient.getGender() == PatientDTO.Gender.M;
        boolean isFemale = patient.getGender() == PatientDTO.Gender.F;


        // Risk level : Borderline
        if (riskTermsFound >=2 && riskTermsFound <= 5 && patientAge >= AGE_THRESHOLD) {
            return RiskLevel.BORDERLINE;
        }

        // Risk level : In Danger
        if ((isMale && patientAge < AGE_THRESHOLD && riskTermsFound == 3) ||
            (isFemale && patientAge < AGE_THRESHOLD && riskTermsFound == 4) ||
            (patientAge >= AGE_THRESHOLD && (riskTermsFound == 6 || riskTermsFound == 7))) {

            return RiskLevel.IN_DANGER;
        }

        // Risk level : Early onset
        if (isMale && patientAge < AGE_THRESHOLD && riskTermsFound >= 5 ||
            isFemale && patientAge < AGE_THRESHOLD && riskTermsFound >= 7 ||
            patientAge >= AGE_THRESHOLD && riskTermsFound >= 8) {

            return RiskLevel.EARLY_ONSET;
        }

        // Risk level : None
        return RiskLevel.NONE;
    }
}