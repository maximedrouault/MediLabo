package com.medilabo.microserviceassessment.proxy;

import com.medilabo.microserviceassessment.config.FeignConfigNote;
import com.medilabo.microserviceassessment.dto.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "GATEWAY-SERVER", contextId = "noteProxy", configuration = FeignConfigNote.class)
public interface MicroserviceNoteProxy {

    @GetMapping("/MICROSERVICE-NOTE/note/list/{patientId}")
    List<NoteDTO> getNotes(@PathVariable Long patientId);

    @GetMapping("/MICROSERVICE-NOTE/note/countRiskTerms/{patientId}")
    Integer countRiskTerms(@PathVariable Long patientId);
}
