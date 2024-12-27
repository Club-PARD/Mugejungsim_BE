package com.example.mugejungsim_be;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



/**
 * CORS 설정 클래스
 *
 * 이 설정은 프론트엔드와 백엔드 간의 Cross-Origin 요청을 허용하기 위해 사용됩니다.
 * Swagger UI에서도 이 설정을 참고하여 API 요청이 성공적으로 이루어질 수 있습니다.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * CORS 정책 정의
     *
     * 이 설정은 다음과 같은 요청을 허용합니다:
     * - 출처: http://localhost:3000
     * - HTTP 메서드: GET, POST, PATCH, DELETE
     * - 모든 헤더 허용
     * - 인증 정보(쿠키, Authorization 헤더 등) 포함
     *
     * @param registry CORS 정책 등록 객체
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
                .allowedOrigins("http://localhost:3000") // 허용할 클라이언트의 도메인
                .allowedMethods("GET", "POST", "PATCH", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true); // 인증 정보(Cookies, Authorization headers 등) 허용
    }
}
