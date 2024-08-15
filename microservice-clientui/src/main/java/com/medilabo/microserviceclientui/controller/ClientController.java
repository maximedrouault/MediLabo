package com.medilabo.microserviceclientui.controller;

import com.medilabo.microserviceclientui.dto.NoteDTO;
import com.medilabo.microserviceclientui.dto.PatientDTO;
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
public class ClientController {

    private final MicroservicePatientProxy microservicePatientProxy;
    private final MicroserviceNoteProxy microserviceNoteProxy;
    private static final String REDIRECT_PATIENT_LIST = "redirect:/patient/list";
    private static final String PATIENTS = "patients";


    @GetMapping("/")
    public String home() {
        return "redirect:/patient/list";
    }


    @GetMapping("/patient/list")
    public String patientList(Model model) {
        List<PatientDTO> patientDTOS = microservicePatientProxy.getAllPatients();
        model.addAttribute(PATIENTS, patientDTOS);

        return "patient/list";
    }

    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        microservicePatientProxy.deletePatientById(id);

        return REDIRECT_PATIENT_LIST;
    }

    @GetMapping("/patient/add")
    public String addPatientForm(Model model) {
        model.addAttribute("patient", PatientDTO.builder().build());

        return "patient/add";
    }

    @PostMapping("/patient/save")
    public String savePatient(@Valid @ModelAttribute("patient") PatientDTO patientDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "patient/add";
        }

        microservicePatientProxy.savePatient(patientDTO);

        return REDIRECT_PATIENT_LIST;
    }

    @GetMapping("/patient/update/{id}")
    public String updatePatientForm(@PathVariable Long id, Model model) {
        PatientDTO patientDTO = microservicePatientProxy.getPatient(id);
        model.addAttribute("patient", patientDTO);

        return "patient/update";
    }

    @PostMapping("/patient/update")
    public String updatePatient(@Valid @ModelAttribute("patient") PatientDTO patientDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "patient/update";
        }

        microservicePatientProxy.updatePatient(patientDTO);

        return REDIRECT_PATIENT_LIST;
    }


    @GetMapping("/note/list/{patientId}")
    public String noteList(@PathVariable Long patientId, Model model) {
        PatientDTO patientDTO = microservicePatientProxy.getPatient(patientId);
        List<NoteDTO> noteDTOS = microserviceNoteProxy.findByPatientIdOrderByCreationDateTimeDesc(patientId);
        model.addAttribute("notes", noteDTOS);
        model.addAttribute("patientFirstName", patientDTO.getFirstName());
        model.addAttribute("patientLastName", patientDTO.getLastName());

        return "note/list";
    }

    @GetMapping("/note/{id}")
    public String noteDetails(@PathVariable String id, Model model) {
        NoteDTO noteDTO = microserviceNoteProxy.getNoteById(id);
        model.addAttribute("note", noteDTO);

        return "note/details";
    }

    @GetMapping("/note/delete/{id}")
    public String deleteNote(@PathVariable String id) {
        NoteDTO noteDTO = microserviceNoteProxy.getNoteById(id);

        microserviceNoteProxy.deleteNoteById(id);

        return "redirect:/note/list/" + noteDTO.getPatientId();
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

        return "redirect:/note/list/" + noteDTO.getPatientId();
    }

    @GetMapping("/note/update/{id}")
    public String updateNoteForm(@PathVariable String id, Model model) {
        NoteDTO noteDTO = microserviceNoteProxy.getNoteById(id);
        model.addAttribute("note", noteDTO);

        return "note/update";
    }

    @PostMapping("/note/update")
    public String updateNote(@Valid @ModelAttribute("note") NoteDTO noteDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "note/update";
        }

        microserviceNoteProxy.updateNoteById(noteDTO);

        return "redirect:/note/list/" + noteDTO.getPatientId();
    }
}