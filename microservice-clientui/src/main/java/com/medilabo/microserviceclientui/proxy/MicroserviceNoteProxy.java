package com.medilabo.microserviceclientui.proxy;

import com.medilabo.microserviceclientui.dto.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "GATEWAY-SERVER", contextId = "noteProxy")
public interface MicroserviceNoteProxy {

    @GetMapping("/MICROSERVICE-NOTE/note/list/{patientId}")
    List<NoteDTO> getNotesByPatientId(@PathVariable Long patientId);

    @DeleteMapping("/MICROSERVICE-NOTE/note/delete/{id}")
    void deleteNoteById(@PathVariable String id);
}
