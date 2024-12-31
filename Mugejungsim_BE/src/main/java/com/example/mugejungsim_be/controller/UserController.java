package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 사용자 저장 또는 업데이트 및 로그인 처리
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveUser(@RequestBody Map<String, String> userData) {
        String name = userData.get("name");
        String provider = userData.get("provider");

        if (name == null || provider == null) {
            return ResponseEntity.badRequest().build(); // 필수 값 누락 시 400 반환
        }

        Long userId = userService.saveOrUpdateUser(name, provider);

        // 사용자 정보와 ID를 Map에 담아 반환
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("name", name);
        response.put("provider", provider);

        return ResponseEntity.ok(response); // 사용자 정보 및 ID 반환
    }


}
