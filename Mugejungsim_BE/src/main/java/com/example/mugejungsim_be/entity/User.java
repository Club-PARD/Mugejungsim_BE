package com.example.mugejungsim_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 사용자 ID (DB 기본 키)

    @Column(nullable = false)
    private String name; // 사용자 이름 또는 닉네임

    @Column(nullable = false)
    private String provider; // 로그인 제공자 (google, kakao)

    // 연관 관계 필드는 그대로 유지
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Story> stories = new ArrayList<>();

    public User(String name, String provider) {
        this.name = name;
        this.provider = provider;
    }
}
