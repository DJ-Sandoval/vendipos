package com.systempos.vendify.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/inicio")
public class HomeController {

    @GetMapping("/home")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String iniciar() {
        return "index";
    }
}
