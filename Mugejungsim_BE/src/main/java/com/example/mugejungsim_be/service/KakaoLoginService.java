package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class KakaoLoginService {

    @Autowired
    private UserRepository userRepository;

    public User processKakaoUser(String name, String profileImage) {
        // 기본값 처리
        name = (name != null && !name.isEmpty()) ? name : "Anonymous User";
        profileImage = (profileImage != null && !profileImage.isEmpty()) ? profileImage : "default-profile.png";

        // 새로운 사용자 생성
        User newUser = new User(name, profileImage, "kakao");
        return userRepository.save(newUser);
    }
}
