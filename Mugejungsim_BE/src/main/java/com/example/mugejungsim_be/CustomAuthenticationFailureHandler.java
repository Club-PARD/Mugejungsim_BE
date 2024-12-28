package com.example.mugejungsim_be;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        // 실패 원인 로그 출력
        System.err.println("Authentication failed: " + exception.getMessage());

        // 실패 원인을 세션에 저장
        request.getSession().setAttribute("error_message", exception.getMessage());

        // 실패 URL로 리다이렉트
        response.sendRedirect("/login?error=true");
    }
}
