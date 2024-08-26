package com.medilabo.microserviceassessment.proxy;

import com.medilabo.microserviceassessment.config.FeignConfigNote;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "GATEWAY-SERVER", contextId = "noteProxy", configuration = FeignConfigNote.class)
public interface MicroserviceNoteProxy {

    @GetMapping("/MICROSERVICE-NOTE/note/countRiskTerms/{patientId}")
    Integer countRiskTerms(@PathVariable Long patientId);
}
