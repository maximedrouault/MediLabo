package com.medilabo.microserviceclientui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ClientController {

    @GetMapping("/")
    public String accueil() {
        return "Accueil";
    }
}
