package com.example.mugejungsim_be.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String handleRootRequest(@AuthenticationPrincipal Object customUser) {
        if (customUser != null) {
            return "redirect:/main"; // Redirects to main page if authenticated
        }
        return "redirect:/login"; // Redirects to login page otherwise
    }
}
