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
    public ResponseEntity<Long> saveUser(@RequestBody Map<String, String> userData) {
        String name = userData.get("name");
        String provider = userData.get("provider");

        if (name == null || provider == null) {
            return ResponseEntity.badRequest().build(); // 필수 값 누락 시 400 반환
        }

        Long userId = userService.saveOrUpdateUser(name, provider);
        return ResponseEntity.ok(userId); // 생성 또는 업데이트된 사용자 ID 반환
    }

    /**
     * 사용자 조회 (name과 provider로)
     */
    @GetMapping("/find")
    public ResponseEntity<User> findUserByNameAndProvider(
            @RequestParam String name,
            @RequestParam String provider) {
        try {
            User user = userService.findByNameAndProvider(name, provider);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 사용자 없을 시 404 반환
        }
    }

    /**
     * 사용자 ID로 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 사용자 미존재 시 404 반환
        }
    }
}
