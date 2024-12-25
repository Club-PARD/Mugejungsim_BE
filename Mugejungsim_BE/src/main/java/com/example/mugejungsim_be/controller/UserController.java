package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // POST 요청
    @PostMapping("/register")
    public ResponseEntity<User> registerOrGetUser(
            @RequestParam String email,
            @RequestParam String name) {
        User user = userService.registerOrGetUser(email, name);
        return ResponseEntity.ok(user);
    }

    // 특정 사용자 조회
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

}
