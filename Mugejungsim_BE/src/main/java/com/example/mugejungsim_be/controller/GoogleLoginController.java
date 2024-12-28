package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.service.GoogleLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * GoogleLoginController
 *
 * 구글 로그인 요청을 처리하는 컨트롤러입니다.
 * 구글 플랫폼으로부터 전달받은 사용자 데이터를 기반으로 사용자 정보를 저장하거나 업데이트합니다.
 */
@RestController
@RequestMapping("/api/google")
public class GoogleLoginController {

    @Autowired
    private GoogleLoginService googleLoginService;

    /**
     * 구글 로그인 요청을 처리합니다.
     *
     * @param googleData 구글로부터 전달받은 사용자 데이터 (name, picture 등).
     * @return 저장된 사용자 정보(User).
     */
    @PostMapping("/login")
    public ResponseEntity<User> loginWithGoogle(@RequestBody Map<String, String> googleData) {
        // 구글 데이터 추출
        String name = googleData.getOrDefault("name", "Anonymous User"); // 이름이 없을 경우 기본값 설정
        String profileImage = googleData.getOrDefault("picture", "default-profile.png"); // 프로필 이미지가 없을 경우 기본값 설정

        // 구글 사용자 정보 처리
        User user = googleLoginService.processGoogleUser(name, profileImage);

        // 처리된 사용자 정보 반환
        return ResponseEntity.ok(user);
    }
}
