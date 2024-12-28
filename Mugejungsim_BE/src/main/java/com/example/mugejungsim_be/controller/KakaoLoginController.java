package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.service.KakaoLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * KakaoLoginController
 *
 * 카카오 로그인 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/kakao")
public class KakaoLoginController {

    @Autowired
    private KakaoLoginService kakaoLoginService;

    /**
     * 카카오 로그인 요청을 처리합니다.
     *
     * @param kakaoData 카카오로부터 전달받은 사용자 데이터 (name 등).
     * @return 저장된 사용자 정보(User).
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginWithKakao(@RequestBody Map<String, String> kakaoData) {
        // 디버깅 로그: 받은 데이터 출력
        System.out.println("Received Kakao Data: " + kakaoData);

        // 사용자 데이터 추출 및 처리
        String name = kakaoData.getOrDefault("name", "Anonymous User");

        // 카카오 사용자 정보 처리
        User user = kakaoLoginService.processKakaoUser(name);

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("provider", user.getProvider());

        // 처리된 사용자 정보 반환
        return ResponseEntity.ok(response);
    }
}
