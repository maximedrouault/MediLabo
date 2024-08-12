package com.medilabo.microservicenote.controller;

import com.medilabo.microservicenote.model.Note;
import com.medilabo.microservicenote.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteRepository noteRepository;


    @GetMapping("/list/{patientId}")
    public ResponseEntity<List<Note>> getNotesByPatientId(@PathVariable Long patientId) {
        return new ResponseEntity<>(noteRepository.findByPatientId(patientId), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Note> addNote(@RequestBody Note note) {
        Note noteCreated = noteRepository.save(note);

        return new ResponseEntity<>(noteCreated, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Note> updateNoteById(@PathVariable String id, @RequestBody Note noteDetails) {
        Optional<Note> noteOptional = noteRepository.findById(id);

        if (noteOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Note existingNote = noteOptional.get();

            existingNote.setPatientId(noteDetails.getPatientId());
            existingNote.setPatientName(noteDetails.getPatientName());
            existingNote.setNoteContent(noteDetails.getNoteContent());

            Note noteUpdated = noteRepository.save(existingNote);

            return new ResponseEntity<>(noteUpdated, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable String id) {
        Optional<Note> noteOptional = noteRepository.findById(id);

        if (noteOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            noteRepository.deleteById(id);

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
