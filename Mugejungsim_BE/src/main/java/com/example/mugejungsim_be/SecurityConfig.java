package com.example.mugejungsim_be;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (API 서버의 경우 필요에 따라 활성화 가능)
                .csrf(csrf -> csrf.disable())

                // 요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/swagger-resources/**", "/webjars/**", "/css/**", "/js/**", "/images/**"
                        ).permitAll() // 공용 리소스 접근 허용
                        .requestMatchers("/", "/login", "/oauth2/**").permitAll() // 홈, 로그인 페이지 접근 허용
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // 사용자 정의 로그인 페이지
                        .defaultSuccessUrl("/main", true) // 로그인 성공 시 메인 페이지로 리디렉션
                        .failureHandler(customAuthenticationFailureHandler) // 로그인 실패 핸들러 등록
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)) // 사용자 정보 처리
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL
                        .logoutSuccessUrl("/login") // 로그아웃 성공 시 로그인 페이지로 이동
                        .invalidateHttpSession(true) // 세션 무효화
                        .clearAuthentication(true) // 인증 정보 삭제
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                );

        return http.build();
    }
}
