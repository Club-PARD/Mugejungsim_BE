package com.example.mugejungsim_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시물(Post) 엔티티 클래스
 * 사용자(User)와 연결되며 여러 스토리(Story)를 포함할 수 있음
 */
@Getter
@Setter
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시물 ID

    @Column(nullable = false)
    private String title; // 게시물 제목

    @Column(nullable = true) // 초기 생성 시 null 허용
    private String bottle; // 게시물의 병 정보 (추가 필드)

    @Column(nullable = false)
    private String startDate; // 여행 시작일

    @Column(nullable = false)
    private String endDate; // 여행 종료일

    @Column(nullable = false)
    private String location; // 여행지

    @Column(nullable = false)
    private String companion; // 여행 동반자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 작성한 사용자와의 관계

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC") // 스토리를 orderIndex 기준으로 정렬
    private List<Story> stories = new ArrayList<>(); // 게시물에 연결된 스토리 목록
}
