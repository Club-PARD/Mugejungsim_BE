package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 사용자(User) 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 사용자를 이메일로 조회하거나 새로 등록합니다.
     *
     * @param email 사용자 이메일
     * @param name  사용자 이름
     * @return 조회되거나 새로 등록된 사용자 객체
     */
    public User registerOrGetUser(String email, String name) {
        // 이메일로 사용자 조회
        return userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(new User(email, name)));
    }

    /**
     * ID로 사용자를 조회합니다.
     *
     * @param id 사용자 ID
     * @return 조회된 사용자 객체
     * @throws RuntimeException 사용자가 존재하지 않을 경우 예외 발생
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }
}
