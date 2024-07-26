package com.medilabo.microserviceclientui.controller;

import com.medilabo.microserviceclientui.dto.PatientDTO;
import com.medilabo.microserviceclientui.proxy.MicroservicePatientProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ClientController {

    private final MicroservicePatientProxy microservicePatientProxy;


    @GetMapping("/")
    public String accueil() {
        return "Accueil";
    }

    @GetMapping("/patients")
    public String patients(Model model) {
        List<PatientDTO> patients = microservicePatientProxy.getPatients();
        model.addAttribute("patients", patients);

        return "Patients";
    }
}
