package com.example.mugejungsim_be;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정 클래스
 *
 * 이 클래스는 OAuth2 인증 및 애플리케이션 보안 정책을 정의합니다.
 * Swagger와의 연동을 위해 특정 경로는 인증 없이 접근 가능하도록 설정됩니다.
 */
@Configuration
@SecurityRequirement(name = "OAuth2")
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    /**
     * SecurityConfig 생성자
     *
     * @param customOAuth2UserService OAuth2 사용자 정보 서비스를 주입받음
     */
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    /**
     * SecurityFilterChain 설정
     *
     * Spring Security의 주요 보안 정책을 정의합니다.
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain 객체
     * @throws Exception 설정 중 오류 발생 시 예외 처리
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (API 서버에서 필요 시 활성화 가능)
                .csrf(csrf -> csrf.disable())

                // 요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // Swagger 및 공용 리소스 허용
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll() // 인증 없이 접근 가능

                        .requestMatchers("/", "/login").permitAll() // 홈 및 로그인 페이지 접근 허용
                        // 특정 경로 인증 필요
                        .requestMatchers("/main", "/api/**").authenticated()
                        .anyRequest().authenticated() // 그 외 모든 요청 인증 필요
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // 사용자 정의 로그인 페이지
                        .defaultSuccessUrl("/main", true) // 로그인 성공 시 메인 페이지로 리디렉션
                        .failureUrl("/login?error=true") // 로그인 실패 시 이동할 URL
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)) // OAuth2 사용자 정보 서비스 설정
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login") // 로그아웃 성공 시 로그인 페이지로 이동
                        .invalidateHttpSession(true) // 세션 무효화
                        .clearAuthentication(true) // 인증 정보 삭제
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                );

        return http.build();
    }
}
