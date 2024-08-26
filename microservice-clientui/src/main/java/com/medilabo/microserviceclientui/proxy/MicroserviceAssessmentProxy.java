package com.medilabo.microserviceclientui.proxy;

import com.medilabo.microserviceclientui.config.FeignConfigAssessment;
import com.medilabo.microserviceclientui.dto.RiskLevelDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "GATEWAY-SERVER", contextId = "assessmentProxy", configuration = FeignConfigAssessment.class)
public interface MicroserviceAssessmentProxy {

    @GetMapping(value = "/MICROSERVICE-ASSESSMENT/assessment/{patientId}")
    RiskLevelDTO getAssessment(@PathVariable Long patientId);
}
