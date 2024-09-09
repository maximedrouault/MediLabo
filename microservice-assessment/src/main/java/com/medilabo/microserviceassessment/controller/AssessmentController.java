package com.medilabo.microserviceassessment.controller;

import com.medilabo.microserviceassessment.dto.PatientDTO;
import com.medilabo.microserviceassessment.model.RiskLevel;
import com.medilabo.microserviceassessment.proxy.MicroserviceNoteProxy;
import com.medilabo.microserviceassessment.proxy.MicroservicePatientProxy;
import com.medilabo.microserviceassessment.service.AssessmentService;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@SecurityRequirement(name = "basicAuth")
public class AssessmentController {

    private final MicroserviceNoteProxy microserviceNoteProxy;
    private final MicroservicePatientProxy microservicePatientProxy;
    private final AssessmentService assessmentService;


    @GetMapping("/assessment/{patientId}")
    @Operation(summary = "Get the risk level of a patient",
            description = "Get the risk level of a patient based on the number of risk terms found in the patient notes",
            parameters = {
            @Parameter(name = "patientId", description = "ID of the patient whose risk level is to be assessed",
                    required = true, example = "1"),
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Risk level assessed"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<RiskLevel> getAssessment(@PathVariable Long patientId) {
        PatientDTO patientDTO;
        Integer riskTermsInPatientNotes;

        try {
            patientDTO = microservicePatientProxy.getPatient(patientId);
            riskTermsInPatientNotes = microserviceNoteProxy.countRiskTerms(patientId);

        } catch (FeignException.NotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        RiskLevel assessment = assessmentService.getAssessment(patientDTO, riskTermsInPatientNotes);

        return new ResponseEntity<>(assessment, HttpStatus.OK);
    }
}