package com.example.mugejungsim_be;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 설정 클래스
 *
 * 다양한 환경에서의 Cross-Origin 요청을 허용하기 위한 설정.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * CORS 정책 정의
     *
     * 이 설정은 다음 요청을 허용합니다:
     * - 로컬 네트워크 및 지정된 IP 주소에서의 요청.
     * - HTTP 메서드: GET, POST, PATCH, PUT, DELETE.
     * - 모든 헤더 허용.
     * - 인증 정보 포함 가능.
     *
     * @param registry CORS 정책 등록 객체
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
                .allowedOrigins(
                        "https://mugejunsim.store", // HTTPS 환경
                        "http://localhost",         // 로컬 환경
                        "http://localhost:8080",    // 로컬 환경에서 명시적인 포트 추가
                        "http://192.168.*.*",
                        "https://www.mugejunsim.store"// 로컬 네트워크 대역 허용
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 쿠키 및 인증 정보 허용
                .maxAge(3600); // 브라우저 캐시 지속 시간 설정 (초 단위)
    }
}
