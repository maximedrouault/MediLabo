package com.medilabo.microserviceassessment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssessmentController {

    @GetMapping("/assessment")
    public String getAssessment() {
        return "Assessment controller works!";
    }
}
