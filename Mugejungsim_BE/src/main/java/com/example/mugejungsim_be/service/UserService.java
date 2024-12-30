package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 사용자 조회: name과 provider를 기반으로 사용자 찾기
     */
    public User findByNameAndProvider(String name, String provider) {
        return userRepository.findByNameAndProvider(name, provider)
                .orElseThrow(() -> new RuntimeException("User not found with name: " + name + " and provider: " + provider));
    }

    /**
     * 사용자 생성 또는 업데이트: name과 provider를 기반으로
     */
    public Long saveOrUpdateUser(String name, String provider) {
        return userRepository.findByNameAndProvider(name, provider)
                .map(User::getId) // 기존 유저가 있으면 ID 반환
                .orElseGet(() -> {
                    User user = new User();
                    user.setName(name);
                    user.setProvider(provider);
                    User savedUser = userRepository.save(user);
                    return savedUser.getId(); // 새로 생성된 유저 ID 반환
                });
    }

    /**
     * ID로 사용자 조회
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
