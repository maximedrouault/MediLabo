package com.medilabo.microserviceassessment.unit.controller;

import com.medilabo.microserviceassessment.controller.AssessmentController;
import com.medilabo.microserviceassessment.dto.PatientDTO;
import com.medilabo.microserviceassessment.model.RiskLevel;
import com.medilabo.microserviceassessment.proxy.MicroserviceNoteProxy;
import com.medilabo.microserviceassessment.proxy.MicroservicePatientProxy;
import com.medilabo.microserviceassessment.service.AssessmentService;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentControllerTest {

    @Mock
    private MicroserviceNoteProxy microserviceNoteProxy;

    @Mock
    private MicroservicePatientProxy microservicePatientProxy;

    @Mock
    private AssessmentService assessmentService;

    @InjectMocks
    private AssessmentController assessmentController;


    // getAssessment
    @Test
    void getAssessmentReturnsRiskLevelWhenPatientExists() {
        Long patientId = 1L;
        PatientDTO patientDTO = PatientDTO.builder().build();
        Integer riskTermsInPatientNotes = 5;
        RiskLevel riskLevel = RiskLevel.BORDERLINE;

        when(microservicePatientProxy.getPatient(patientId)).thenReturn(patientDTO);
        when(microserviceNoteProxy.countRiskTerms(patientId)).thenReturn(riskTermsInPatientNotes);
        when(assessmentService.getAssessment(patientDTO, riskTermsInPatientNotes)).thenReturn(riskLevel);

        ResponseEntity<RiskLevel> response = assessmentController.getAssessment(patientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(riskLevel, response.getBody());
    }

    @Test
    void getAssessmentReturnsNotFoundWhenPatientDoesNotExist() {
        Long patientId = 1L;

        when(microservicePatientProxy.getPatient(patientId)).thenThrow(FeignException.NotFound.class);

        ResponseEntity<RiskLevel> response = assessmentController.getAssessment(patientId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
