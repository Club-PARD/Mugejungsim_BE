package com.example.mugejungsim_be;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 설정 클래스
 *
 * 이 설정은 로컬 네트워크에서 iOS 앱과의 Cross-Origin 요청을 허용하기 위해 사용됩니다.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * CORS 정책 정의
     *
     * 이 설정은 다음과 같은 요청을 허용합니다:
     * - iOS 앱의 IP 또는 로컬 네트워크 환경 (http://192.168.1.22:8080)
     * - HTTP 메서드: GET, POST, PATCH, DELETE
     * - 모든 헤더 허용
     * - 인증 정보 포함 여부 설정
     *
     * @param registry CORS 정책 등록 객체
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
                .allowedOrigins(
                        "http://localhost", // iOS 앱에서 로컬 네트워크로 접근
                        "http://192.168.1.22" // 백엔드의 IP 주소 (네트워크 상)
                )
                .allowedMethods("GET", "POST", "PATCH", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(false); // 쿠키/인증 정보는 필요 없으므로 false로 설정
    }
}
