package com.medilabo.microserviceclientui.controller;

import com.medilabo.microserviceclientui.dto.NoteDTO;
import com.medilabo.microserviceclientui.dto.PatientDTO;
import com.medilabo.microserviceclientui.dto.RiskLevelDTO;
import com.medilabo.microserviceclientui.proxy.MicroserviceAssessmentProxy;
import com.medilabo.microserviceclientui.proxy.MicroserviceNoteProxy;
import com.medilabo.microserviceclientui.proxy.MicroservicePatientProxy;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class NoteViewController {

    private final MicroservicePatientProxy microservicePatientProxy;
    private final MicroserviceNoteProxy microserviceNoteProxy;
    private final MicroserviceAssessmentProxy microserviceAssessmentProxy;

    private static final String REDIRECT_NOTE_LIST = "redirect:/note/list/";


    @GetMapping("/note/list/{patientId}")
    public String noteList(@PathVariable Long patientId, Model model) {
        PatientDTO patientDTO = microservicePatientProxy.getPatient(patientId);
        List<NoteDTO> noteDTOS = microserviceNoteProxy.getNotes(patientId);
        RiskLevelDTO riskLevelDTO = microserviceAssessmentProxy.getAssessment(patientId);

        model.addAttribute("riskLevel", riskLevelDTO);
        model.addAttribute("notes", noteDTOS);
        model.addAttribute("patientFirstName", patientDTO.getFirstName());
        model.addAttribute("patientLastName", patientDTO.getLastName());

        return "note/list";
    }

    @GetMapping("/note/{id}")
    public String noteDetails(@PathVariable String id, Model model) {
        NoteDTO noteDTO = microserviceNoteProxy.getNote(id);
        model.addAttribute("note", noteDTO);

        return "note/details";
    }

    @GetMapping("/note/delete/{id}")
    public String deleteNote(@PathVariable String id) {
        NoteDTO noteDTO = microserviceNoteProxy.getNote(id);

        microserviceNoteProxy.deleteNote(id);

        return REDIRECT_NOTE_LIST + noteDTO.getPatientId();
    }

    @GetMapping("/note/add/{patientId}")
    public String addNoteForm(@PathVariable Long patientId, Model model) {
        PatientDTO patientDTO = microservicePatientProxy.getPatient(patientId);

        model.addAttribute("note", NoteDTO.builder()
                        .patientId(patientId)
                        .patientName(patientDTO.getLastName())
                .build());

        return "note/add";
    }

    @PostMapping("/note/save")
    public String saveNote(@Valid @ModelAttribute("note") NoteDTO noteDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "note/add";
        }

        microserviceNoteProxy.saveNote(noteDTO);

        return REDIRECT_NOTE_LIST + noteDTO.getPatientId();
    }

    @GetMapping("/note/update/{id}")
    public String updateNoteForm(@PathVariable String id, Model model) {
        NoteDTO noteDTO = microserviceNoteProxy.getNote(id);
        model.addAttribute("note", noteDTO);

        return "note/update";
    }

    @PostMapping("/note/update")
    public String updateNote(@Valid @ModelAttribute("note") NoteDTO noteDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "note/update";
        }

        microserviceNoteProxy.updateNote(noteDTO);

        return REDIRECT_NOTE_LIST + noteDTO.getPatientId();
    }
}