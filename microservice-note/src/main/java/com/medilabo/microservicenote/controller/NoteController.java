package com.medilabo.microservicenote.controller;

import com.medilabo.microservicenote.model.Note;
import com.medilabo.microservicenote.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    @Value("${riskTerms}")
    private String riskTerms;

    private final NoteRepository noteRepository;


    @GetMapping("/list/{patientId}")
    public ResponseEntity<List<Note>> getNotes(@PathVariable Long patientId) {
        List<Note> notes = noteRepository.findByPatientId(patientId, Sort.by(Sort.Direction.DESC, "creationDateTime"));

        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNote(@PathVariable String id) {
        Optional<Note> noteOptional = noteRepository.findById(id);

        return noteOptional.map(note ->
                new ResponseEntity<>(note, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<Note> addNote(@RequestBody Note note) {
        note.setCreationDateTime(LocalDateTime.now());

        Note noteCreated = noteRepository.save(note);

        return new ResponseEntity<>(noteCreated, HttpStatus.CREATED);
    }

    @PutMapping("/update")
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
    public ResponseEntity<Void> deleteNotes(@PathVariable Long patientId) {
        noteRepository.deleteAllByPatientId(patientId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/countRiskTerms/{patientId}")
    public ResponseEntity<Integer> countRiskTerms(@PathVariable Long patientId) {
        Integer riskTermsFound = noteRepository.countRiskTerms(patientId, riskTerms).orElse(0);

        return new ResponseEntity<>(riskTermsFound, HttpStatus.OK);
    }
}