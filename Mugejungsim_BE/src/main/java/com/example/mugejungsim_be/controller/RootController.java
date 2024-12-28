package com.example.mugejungsim_be.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * RootController
 *
 * 루트 URL("/") 요청을 처리하는 컨트롤러입니다.
 * 사용자의 인증 상태에 따라 적절한 페이지로 리디렉션합니다.
 */
@Controller
public class RootController {

    /**
     * 루트 URL("/") 요청을 처리합니다.
     * - 인증된 사용자는 메인 페이지("/main")로 리디렉션됩니다.
     * - 인증되지 않은 사용자는 로그인 페이지("/login")로 리디렉션됩니다.
     *
     * @param customUser 인증된 사용자 객체 (없으면 null).
     * @return 인증 상태에 따라 적절한 페이지로 리디렉션.
     */
    @GetMapping("/")
    public String handleRootRequest(@AuthenticationPrincipal Object customUser) {
        if (customUser != null) {
            // 인증된 사용자: 메인 페이지로 리디렉션
            return "redirect:/main";
        }
        // 인증되지 않은 사용자: 로그인 페이지로 리디렉션
        return "redirect:/login";
    }
}
