package com.medilabo.microserviceassessment.controller;

import com.medilabo.microserviceassessment.dto.PatientDTO;
import com.medilabo.microserviceassessment.model.RiskLevel;
import com.medilabo.microserviceassessment.proxy.MicroserviceNoteProxy;
import com.medilabo.microserviceassessment.proxy.MicroservicePatientProxy;
import com.medilabo.microserviceassessment.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AssessmentController {

    private final MicroserviceNoteProxy microserviceNoteProxy;
    private final MicroservicePatientProxy microservicePatientProxy;
    private final AssessmentService assessmentService;


    @GetMapping("/assessment/{patientId}")
    public ResponseEntity<RiskLevel> getAssessment(@PathVariable Long patientId) {
        PatientDTO patientDTO = microservicePatientProxy.getPatient(patientId);
        int riskTermsInPatientNotes = microserviceNoteProxy.countRiskTerms(patientId);

        RiskLevel assessment = assessmentService.getAssessment(patientDTO, riskTermsInPatientNotes);

        return new ResponseEntity<>(assessment, HttpStatus.OK);
    }
}