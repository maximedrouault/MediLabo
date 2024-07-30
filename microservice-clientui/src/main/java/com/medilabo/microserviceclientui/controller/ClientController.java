package com.medilabo.microserviceclientui.controller;

import com.medilabo.microserviceclientui.dto.PatientDTO;
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
    private static final String REDIRECT_PATIENT_LIST = "redirect:/patient/list";
    private static final String PATIENTS = "patients";



    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/patient/list")
    public String patientList(Model model) {
        List<PatientDTO> patients = microservicePatientProxy.getAllPatients();
        model.addAttribute(PATIENTS, patients);

        return "patient/list";
    }

    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        microservicePatientProxy.deletePatient(id);

        return REDIRECT_PATIENT_LIST;
    }

    @GetMapping("/patient/add")
    public String addPatientForm(Model model) {
        model.addAttribute("patient", PatientDTO.builder().build());

        return "patient/add";
    }

    @PostMapping("/patient/save")
    public String savePatient(@Valid @ModelAttribute("patient") PatientDTO patientDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "patient/add";
        }

        microservicePatientProxy.savePatient(patientDTO);
        model.addAttribute(PATIENTS, microservicePatientProxy.getAllPatients());

        return REDIRECT_PATIENT_LIST;
    }

    @GetMapping("/patient/update/{id}")
    public String updatePatientForm(@PathVariable Long id, Model model) {
        PatientDTO patientDTO = microservicePatientProxy.getPatient(id);
        model.addAttribute("patient", patientDTO);

        return "patient/update";
    }

    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable Long id, @Valid @ModelAttribute("patient") PatientDTO patientDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "patient/update";
        }

        patientDTO.setId(id);
        microservicePatientProxy.savePatient(patientDTO);
        model.addAttribute(PATIENTS, microservicePatientProxy.getAllPatients());

        return REDIRECT_PATIENT_LIST;
    }
}