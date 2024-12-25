package com.example.mugejungsim_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "Login", description = "User Login Page")
public class LoginController {

    @Operation(summary = "User Login Page", description = "Redirects the user to the OAuth2 login page.")
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Navigates to the login page
    }
}
