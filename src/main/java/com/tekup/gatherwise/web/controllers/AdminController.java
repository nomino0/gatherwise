package com.tekup.gatherwise.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class AdminController {

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard"; // Name of the HTML/Thymeleaf file for the admin dashboard
    }
}