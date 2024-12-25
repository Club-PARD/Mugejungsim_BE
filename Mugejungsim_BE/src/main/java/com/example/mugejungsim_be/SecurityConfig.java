package com.example.mugejungsim_be;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@SecurityRequirement(name = "OAuth2")
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화
                .csrf(csrf -> csrf.disable())

                // 요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll() // Swagger 관련 경로 허용
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll() // 로그인 페이지와 정적 리소스 허용
                        .requestMatchers("/main", "/api/stories/**").authenticated() // 인증 필요
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/main", true) // 로그인 성공 시 메인 페이지로 이동
                        .loginPage("/login") // 사용자 정의 로그인 페이지
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)) // CustomOAuth2UserService 연결
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login") // 로그아웃 후 로그인 페이지로 이동
                        .invalidateHttpSession(true) // 세션 무효화
                        .clearAuthentication(true) // 인증 정보 삭제
                );

        return http.build();
    }
}
