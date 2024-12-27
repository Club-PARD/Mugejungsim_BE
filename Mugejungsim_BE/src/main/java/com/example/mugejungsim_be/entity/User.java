package com.example.mugejungsim_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자(User) 엔티티 클래스
 * 게시물(Post) 및 스토리(Story)와 연결됨
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 ID

    @Column(nullable = false, unique = true)
    private String email; // 사용자 이메일 (고유 값)

    @Column(nullable = false)
    private String name; // 사용자 이름

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>(); // 사용자가 작성한 게시물 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Story> stories = new ArrayList<>(); // 사용자가 작성한 스토리 목록

    /**
     * 사용자 생성자
     * @param email 사용자 이메일
     * @param name  사용자 이름
     */
    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
