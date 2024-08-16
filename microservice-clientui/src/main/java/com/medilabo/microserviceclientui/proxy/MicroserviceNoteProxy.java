package com.medilabo.microserviceclientui.proxy;

import com.medilabo.microserviceclientui.config.FeignConfigNote;
import com.medilabo.microserviceclientui.dto.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "GATEWAY-SERVER", contextId = "noteProxy", configuration = FeignConfigNote.class)
public interface MicroserviceNoteProxy {

    @GetMapping("/MICROSERVICE-NOTE/note/list/{patientId}")
    List<NoteDTO> findByPatientIdOrderByCreationDateTimeDesc(@PathVariable Long patientId);

    @GetMapping("/MICROSERVICE-NOTE/note/{id}")
    NoteDTO getNoteById(@PathVariable String id);

    @DeleteMapping("/MICROSERVICE-NOTE/note/delete/{id}")
    void deleteNoteById(@PathVariable String id);

    @PostMapping("/MICROSERVICE-NOTE/note/add")
    NoteDTO saveNote(@RequestBody NoteDTO note);

    @PutMapping("/MICROSERVICE-NOTE/note/update")
    NoteDTO updateNoteById(@RequestBody NoteDTO noteDetails);
}
