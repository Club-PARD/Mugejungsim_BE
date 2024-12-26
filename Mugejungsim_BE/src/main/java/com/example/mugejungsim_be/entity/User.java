package com.example.mugejungsim_be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // 테이블 이름을 "users"로 명시적으로 지정 (PostgreSQL 등과 호환)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // Story와 1:N 관계
    private List<Story> stories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // Post와 1:N 관계
    private List<Post> posts;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
