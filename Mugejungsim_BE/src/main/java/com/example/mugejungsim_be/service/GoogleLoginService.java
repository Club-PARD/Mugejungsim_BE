package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleLoginService {

    @Autowired
    private UserRepository userRepository;

    public User processGoogleUser(String name) {
        // 기본값 처리
        name = (name != null && !name.isEmpty()) ? name : "Anonymous User";


        // 신규 사용자 생성
        User newUser = new User(name, "google");
        return userRepository.save(newUser);
    }
}
