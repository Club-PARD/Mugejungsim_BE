package com.example.mugejungsim_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * LoginController
 *
 * 사용자 로그인 페이지를 제공하는 컨트롤러입니다.
 */
@Controller
@Tag(name = "Login", description = "User Login Page")
public class LoginController {

    /**
     * 로그인 페이지로 리다이렉트합니다.
     *
     * @return 로그인 페이지 경로
     */
    @Operation(summary = "User Login Page", description = "Redirects the user to the OAuth2 login page.")
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // login.html 뷰로 이동
    }
}
