package com.tekup.gatherwise.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class AuthController {
    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/access-denied";
        }
        return "login";
    }
    
    @GetMapping("/access-denied")
    public String getAccessDeniedPage(Model model) {
     
        model.addAttribute("error", "You are not allowed to access this page");
        return "errors";
    }

    
}