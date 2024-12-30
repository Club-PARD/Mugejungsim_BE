package com.example.mugejungsim_be.repository;

import com.example.mugejungsim_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNameAndProvider(String name, String provider);
}
