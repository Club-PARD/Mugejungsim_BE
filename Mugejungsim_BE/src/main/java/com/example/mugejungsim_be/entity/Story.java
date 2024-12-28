package com.example.mugejungsim_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 스토리(Story) 엔티티 클래스
 */
@Getter
@Setter
@Entity
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 스토리 ID

    @Column(nullable = false)
    private String content; // 스토리 내용

    @ElementCollection
    @CollectionTable(name = "story_categories", joinColumns = @JoinColumn(name = "story_id"))
    @Column(name = "category")
    private List<String> categories; // 다중 카테고리 지원

    private String imagePath; // 이미지 경로 (선택 사항)

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer orderIndex; // 스토리 정렬을 위한 순서 인덱스

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 작성한 사용자와의 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post; // 연결된 게시물과의 관계 (선택 사항)
}
