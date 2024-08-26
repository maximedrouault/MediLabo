package com.medilabo.microserviceclientui.controller;

import com.medilabo.microserviceclientui.dto.PatientDTO;
import com.medilabo.microserviceclientui.proxy.MicroserviceNoteProxy;
import com.medilabo.microserviceclientui.proxy.MicroservicePatientProxy;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
public class PatientViewController {

    private final MicroservicePatientProxy microservicePatientProxy;
    private final MicroserviceNoteProxy microserviceNoteProxy;


    @GetMapping("/patient/list")
    public String patientList(Model model) {
        List<PatientDTO> patientDTOS = microservicePatientProxy.getAllPatients();
        model.addAttribute("patients", patientDTOS);

        return "patient/list";
    }

    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        ResponseEntity<Void> response = microserviceNoteProxy.deleteNotes(id);

        if (response.getStatusCode().is2xxSuccessful()) {
            microservicePatientProxy.deletePatient(id);
        }

        return "redirect:/patient/list";
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

        return "redirect:/patient/list";
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

        return "redirect:/patient/list";
    }
}