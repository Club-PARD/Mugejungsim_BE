package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class KakaoLoginService {

    @Autowired
    private UserRepository userRepository;

    public User processKakaoUser(String name) {
        // 기본값 처리
        String validatedName = StringUtils.hasText(name) ? name : "Anonymous User";

        // 새로운 사용자 생성
        User newUser = new User(validatedName, "kakao");

        // 사용자 저장 및 반환
        return userRepository.save(newUser);
    }
}
