package com.medilabo.microserviceclientui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

    @GetMapping("/")
    public String home() {
        return "redirect:/patient/list";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
