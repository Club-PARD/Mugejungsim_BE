package com.example.mugejungsim_be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 사용자 ID

    @Column(nullable = false)
    private String name; // 사용자 이름 또는 닉네임

    @Column(nullable = false)
    private String provider; // 로그인 제공자 (예: kakao)

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>(); // 사용자가 작성한 게시물

    // Story와의 직접적인 관계 제거 (Post를 통해 간접적으로 참조 가능)

    public User(String name, String provider) {
        this.name = name;
        this.provider = provider;
    }
}