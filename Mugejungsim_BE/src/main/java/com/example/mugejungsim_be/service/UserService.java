package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 사용자 저장 또는 업데이트
    public User saveOrUpdateUser(User user) {
        if (user.getId() != null) {
            // 기존 사용자가 존재하면 업데이트
            return userRepository.findById(user.getId())
                    .map(existingUser -> {
                        existingUser.setName(user.getName());
                        existingUser.setProfileImageUrl(user.getProfileImageUrl());
                        existingUser.setProvider(user.getProvider());
                        return userRepository.save(existingUser);
                    })
                    .orElseGet(() -> userRepository.save(user)); // 기존 사용자가 없으면 새로 저장
        } else {
            // 새로운 사용자 저장
            return userRepository.save(user);
        }
    }

    // ID로 사용자 조회
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
