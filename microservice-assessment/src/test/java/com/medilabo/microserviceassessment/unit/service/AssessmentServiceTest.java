package com.medilabo.microserviceassessment.unit.service;


import com.medilabo.microserviceassessment.dto.PatientDTO;
import com.medilabo.microserviceassessment.model.RiskLevel;
import com.medilabo.microserviceassessment.service.AssessmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {

    @InjectMocks
    private AssessmentService assessmentService;


    // Borderline
    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5})
    void getAssessmentReturnsBorderlineWhenRiskTermsBetween2And5AndAgeAboveThreshold(int riskTermsFound) {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .gender(PatientDTO.Gender.M).build();

        RiskLevel result = assessmentService.getAssessment(patient, riskTermsFound);

        assertEquals(RiskLevel.BORDERLINE, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5})
    void getAssessmentReturnsBorderlineWhenRiskTermsBetween2And5AndAgeExactlyThreshold(int riskTermsFound) {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.now().minusYears(30))
                .gender(PatientDTO.Gender.M).build();

        RiskLevel result = assessmentService.getAssessment(patient, riskTermsFound);

        assertEquals(RiskLevel.BORDERLINE, result);
    }

    // In Danger
    @Test
    void getAssessmentReturnsInDangerWhenMaleAgeBelowThresholdAndRiskTerms3() {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender(PatientDTO.Gender.M).build();

        RiskLevel result = assessmentService.getAssessment(patient, 3);

        assertEquals(RiskLevel.IN_DANGER, result);
    }

    @Test
    void getAssessmentReturnsInDangerWhenFemaleAgeBelowThresholdAndRiskTerms4() {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender(PatientDTO.Gender.F).build();

        RiskLevel result = assessmentService.getAssessment(patient, 4);

        assertEquals(RiskLevel.IN_DANGER, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7})
    void getAssessmentReturnsInDangerWhenAgeAboveThresholdAndRiskTerms6Or7(int riskTermsFound) {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .gender(PatientDTO.Gender.M).build();

        RiskLevel result = assessmentService.getAssessment(patient, riskTermsFound);

        assertEquals(RiskLevel.IN_DANGER, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7})
    void getAssessmentReturnsInDangerWhenAgeExactlyThresholdAndRiskTerms6Or7(int riskTermsFound) {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.now().minusYears(30))
                .gender(PatientDTO.Gender.F).build();

        RiskLevel result = assessmentService.getAssessment(patient, riskTermsFound);

        assertEquals(RiskLevel.IN_DANGER, result);
    }

    // Early Onset
    @ParameterizedTest
    @ValueSource(ints = {5, 6, 7, 8, 9})
    void getAssessmentReturnsEarlyOnsetWhenMaleAgeBelowThresholdAndRiskTerms5OrMore(int riskTermsFound) {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender(PatientDTO.Gender.M).build();

        RiskLevel result = assessmentService.getAssessment(patient, riskTermsFound);

        assertEquals(RiskLevel.EARLY_ONSET, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 8, 9})
    void getAssessmentReturnsEarlyOnsetWhenFemaleAgeBelowThresholdAndRiskTerms7OrMore(int riskTermsFound) {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender(PatientDTO.Gender.F).build();

        RiskLevel result = assessmentService.getAssessment(patient, riskTermsFound);

        assertEquals(RiskLevel.EARLY_ONSET, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9})
    void getAssessmentReturnsEarlyOnsetWhenAgeAboveThresholdAndRiskTerms8OrMore(int riskTermsFound) {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .gender(PatientDTO.Gender.M).build();

        RiskLevel result = assessmentService.getAssessment(patient, riskTermsFound);

        assertEquals(RiskLevel.EARLY_ONSET, result);
    }

    // None
    @Test
    void getAssessmentReturnsNoneWhenNoConditionsMet() {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender(PatientDTO.Gender.M).build();

        RiskLevel result = assessmentService.getAssessment(patient, 1);

        assertEquals(RiskLevel.NONE, result);
    }

    @Test
    void getAssessmentReturnsNoneWhenRiskTermsNegative() {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender(PatientDTO.Gender.M).build();

        RiskLevel result = assessmentService.getAssessment(patient, -1);

        assertEquals(RiskLevel.NONE, result);
    }

    @Test
    void getAssessmentReturnsNoneWhenRiskTermsZero() {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.of(1995, 1, 1))
                .gender(PatientDTO.Gender.F).build();

        RiskLevel result = assessmentService.getAssessment(patient, 0);

        assertEquals(RiskLevel.NONE, result);
    }

    @Test
    void getAssessmentReturnsNoneWhenDateOfBirthInFuture() {
        PatientDTO patient = PatientDTO.builder()
                .dateOfBirth(LocalDate.now().plusYears(1))
                .gender(PatientDTO.Gender.M).build();

        RiskLevel result = assessmentService.getAssessment(patient, 2);

        assertEquals(RiskLevel.NONE, result);
    }
}
