package com.medilabo.microservicenote.unit.controller;

import com.medilabo.microservicenote.controller.NoteController;
import com.medilabo.microservicenote.model.Note;
import com.medilabo.microservicenote.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteController noteController;


    // getNotes
    @Test
    void getNotesReturnsNotesListWhenPatientIdExists() {
        Long patientId = 1L;
        Note note1 = Note.builder().build();
        Note note2 = Note.builder().build();
        List<Note> notes = List.of(note1, note2);

        when(noteRepository.findByPatientId(patientId, Sort.by(Sort.Direction.DESC, "creationDateTime"))).thenReturn(notes);

        ResponseEntity<List<Note>> response = noteController.getNotes(patientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notes, response.getBody());
    }

    @Test
    void getNotesReturnsEmptyListWhenNoNotesExistForPatientId() {
        Long patientId = 1L;

        when(noteRepository.findByPatientId(patientId, Sort.by(Sort.Direction.DESC, "creationDateTime"))).thenReturn(Collections.emptyList());

        ResponseEntity<List<Note>> response = noteController.getNotes(patientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    // getNote
    @Test
    void getNoteReturnsNoteWhenIdExists() {
        String noteId = "noteId1";
        Note note = Note.builder().id(noteId).build();
        Optional<Note> noteOptional = Optional.of(note);

        when(noteRepository.findById(noteId)).thenReturn(noteOptional);

        ResponseEntity<Note> response = noteController.getNote(noteId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(note, response.getBody());
    }

    @Test
    void getNoteReturnsNotFoundWhenIdDoesNotExist() {
        String noteId = "noteId1";

        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        ResponseEntity<Note> response = noteController.getNote(noteId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // addNote
    @Test
    void addNoteReturnsCreatedNote() {
        Note note = Note.builder().patientId(1L).patientName("John Doe").noteContent("Sample note").build();
        Note savedNote = Note.builder().id("noteId1").patientId(1L).patientName("John Doe").noteContent("Sample note").creationDateTime(LocalDateTime.now()).build();

        when(noteRepository.save(note)).thenReturn(savedNote);

        ResponseEntity<Note> response = noteController.addNote(note);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedNote, response.getBody());
    }

    @Test
    void addNoteSetsCreationDateTime() {
        Note note = Note.builder().patientId(1L).patientName("John Doe").noteContent("Sample note").build();

        noteController.addNote(note);

        assertNotNull(note.getCreationDateTime());
    }

    // updateNote
    @Test
    void updateNoteReturnsUpdatedNoteWhenIdExists() {
        Note noteDetails = Note.builder().id("noteId1").patientId(1L).patientName("John Doe").noteContent("Updated content").build();
        Note existingNote = Note.builder().id("noteId1").patientId(1L).patientName("John Doe").noteContent("Original content").creationDateTime(LocalDateTime.now()).build();

        when(noteRepository.findById(noteDetails.getId())).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(existingNote)).thenReturn(existingNote);

        ResponseEntity<Note> response = noteController.updateNote(noteDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingNote, response.getBody());
    }

    @Test
    void updateNoteReturnsNotFoundWhenIdDoesNotExist() {
        Note noteDetails = Note.builder().id("noteId1").patientId(1L).patientName("John Doe").noteContent("Updated content").build();

        when(noteRepository.findById(noteDetails.getId())).thenReturn(Optional.empty());

        ResponseEntity<Note> response = noteController.updateNote(noteDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // deleteNote
    @Test
    void deleteNoteReturnsOkWhenIdExists() {
        String noteId = "noteId1";
        Note note = Note.builder().id(noteId).build();

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));

        ResponseEntity<Void> response = noteController.deleteNote(noteId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(noteRepository, times(1)).deleteById(noteId);
    }

    @Test
    void deleteNoteReturnsNotFoundWhenIdDoesNotExist() {
        String noteId = "noteId1";

        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = noteController.deleteNote(noteId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(noteRepository, times(0)).deleteById(noteId);
    }

    // deleteNotes
    @Test
    void deleteNotesReturnsOkWhenPatientIdExists() {
        Long patientId = 1L;

        ResponseEntity<Void> response = noteController.deleteNotes(patientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(noteRepository, times(1)).deleteAllByPatientId(patientId);
    }

    @Test
    void deleteNotesHandlesEmptyRepository() {
        Long patientId = 1L;

        doThrow(new RuntimeException("Repository is empty")).when(noteRepository).deleteAllByPatientId(patientId);

        assertThrows(RuntimeException.class, () -> noteController.deleteNotes(patientId));
    }

    // countRiskTerms
    @Test
    void countRiskTermsReturnsCountWhenPatientIdExists() {
        Long patientId = 1L;
        Integer riskTermsCount = 5;
        String mockRiskTerms = "riskTerms1|riskTerms2";

        ReflectionTestUtils.setField(noteController, "riskTerms", mockRiskTerms);
        when(noteRepository.countRiskTerms(patientId, mockRiskTerms)).thenReturn(Optional.of(riskTermsCount));

        ResponseEntity<Integer> response = noteController.countRiskTerms(patientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(riskTermsCount, response.getBody());
    }

    @Test
    void countRiskTermsReturnsZeroWhenNoRiskTermsFound() {
        Long patientId = 1L;
        String mockRiskTerms = "riskTerms1|riskTerms2";

        ReflectionTestUtils.setField(noteController, "riskTerms", mockRiskTerms);
        when(noteRepository.countRiskTerms(patientId, mockRiskTerms)).thenReturn(Optional.empty());

        ResponseEntity<Integer> response = noteController.countRiskTerms(patientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody());
    }
}
