package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.service.KakaoLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * KakaoLoginController
 *
 * 카카오 로그인 요청을 처리하는 컨트롤러입니다.
 * 카카오 플랫폼으로부터 전달받은 사용자 데이터를 기반으로 사용자 정보를 저장하거나 업데이트합니다.
 */
@RestController
@RequestMapping("/api/kakao")
public class KakaoLoginController {

    @Autowired
    private KakaoLoginService kakaoLoginService;

    /**
     * 카카오 로그인 요청을 처리합니다.
     *
     * @param kakaoData 카카오로부터 전달받은 사용자 데이터 (name, profileImage 등).
     * @return 저장된 사용자 정보(User).
     */
    @PostMapping("/login")
    public ResponseEntity<User> loginWithKakao(@RequestBody Map<String, String> kakaoData) {
        // 디버깅 로그: 받은 데이터 출력
        System.out.println("Received Kakao Data: " + kakaoData);

        // 사용자 데이터 추출
        String name = kakaoData.getOrDefault("name", "Anonymous User"); // 이름이 없으면 "Anonymous User"로 설정
        String profileImage = kakaoData.getOrDefault("profileImage", "default-profile.png"); // 이미지가 없으면 기본 이미지 설정

        // 카카오 사용자 정보 처리
        User user = kakaoLoginService.processKakaoUser(name, profileImage);

        // 처리된 사용자 정보 반환
        return ResponseEntity.ok(user);
    }
}
