package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.CustomOAuth2User;
import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 사용자 저장 또는 업데이트
     */
    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = userService.saveOrUpdateUser(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * 사용자 ID로 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal CustomOAuth2User customUser) {
        if (customUser == null) {
            return ResponseEntity.status(401).body(null);
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", customUser.getId());
        userInfo.put("name", customUser.getName());
        userInfo.put("provider", customUser.getUser().getProvider());

        return ResponseEntity.ok(userInfo);
    }
}

