package com.medilabo.microservicenote.controller;

import com.medilabo.microservicenote.model.Note;
import com.medilabo.microservicenote.repository.NoteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@SecurityRequirement(name = "basicAuth")
public class NoteController {

    @Value("${riskTerms}")
    private String riskTerms;

    private final NoteRepository noteRepository;


    @GetMapping("/list/{patientId}")
    @Operation(summary = "Get all notes of a patient", parameters = {
            @Parameter(name = "patientId", description = "ID of the patient whose notes are to be retrieved", required = true, example = "1"),
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Notes found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<Note>> getNotes(@PathVariable Long patientId) {
        List<Note> notes = noteRepository.findByPatientId(patientId, Sort.by(Sort.Direction.DESC, "creationDateTime"));

        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a note by id", parameters = {
            @Parameter(name = "id", description = "ID of the note to be retrieved", required = true, example = "66d6e054ee801a4ea25e739c"),
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Note found"),
            @ApiResponse(responseCode = "404", description = "Note not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")

    })
    public ResponseEntity<Note> getNote(@PathVariable String id) {
        Optional<Note> noteOptional = noteRepository.findById(id);

        return noteOptional.map(note ->
                new ResponseEntity<>(note, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    @Operation(summary = "Add a note",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Note details to be added", required = true),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Note added"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    public ResponseEntity<Note> addNote(@RequestBody Note note) {
        note.setCreationDateTime(LocalDateTime.now());

        Note noteCreated = noteRepository.save(note);

        return new ResponseEntity<>(noteCreated, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @Operation(summary = "Update a note",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Note details to be updated", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Note updated"),
                    @ApiResponse(responseCode = "404", description = "Note not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    public ResponseEntity<Note> updateNote(@RequestBody Note noteDetails) {
        Optional<Note> noteOptional = noteRepository.findById(noteDetails.getId());

        if (noteOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Note existingNote = noteOptional.get();

            existingNote.setPatientId(noteDetails.getPatientId());
            existingNote.setCreationDateTime(existingNote.getCreationDateTime());
            existingNote.setPatientName(noteDetails.getPatientName());
            existingNote.setNoteContent(noteDetails.getNoteContent());

            Note noteUpdated = noteRepository.save(existingNote);

            return new ResponseEntity<>(noteUpdated, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a note by id", parameters = {
            @Parameter(name = "id", description = "ID of the note to be deleted", required = true, example = "66d6e054ee801a4ea25e739c"),
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Note deleted"),
            @ApiResponse(responseCode = "404", description = "Note not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        Optional<Note> noteOptional = noteRepository.findById(id);

        if (noteOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            noteRepository.deleteById(id);

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteAll/{patientId}")
    @Operation(summary = "Delete all notes of a patient", parameters = {
            @Parameter(name = "patientId", description = "ID of the patient whose notes are to be deleted", required = true, example = "1"),
    }, responses = {
            @ApiResponse(responseCode = "200", description = "All notes deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteNotes(@PathVariable Long patientId) {
        noteRepository.deleteAllByPatientId(patientId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/countRiskTerms/{patientId}")
    @Operation(summary = "Count risk terms in notes of a patient", parameters = {
            @Parameter(name = "patientId", description = "ID of the patient whose notes are to be searched for risk terms", required = true, example = "1"),
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Risk terms found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Integer> countRiskTerms(@PathVariable Long patientId) {
        Integer riskTermsFound = noteRepository.countRiskTerms(patientId, riskTerms).orElse(0);

        return new ResponseEntity<>(riskTermsFound, HttpStatus.OK);
    }
}