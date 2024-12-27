package com.example.mugejungsim_be.dto;

import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.Story;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시물(Post) 데이터를 전달하기 위한 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    /**
     * 게시물 ID
     */
    private Long id;

    /**
     * 게시물 제목
     */
    private String title;

    /**
     * 게시물 설명
     */
    private String description;

    /**
     * 여행 시작 날짜
     */
    private String startDate;

    /**
     * 여행 종료 날짜
     */
    private String endDate;

    /**
     * 여행지 위치
     */
    private String location;

    /**
     * 여행 동반자 정보
     */
    private String companion;

    /**
     * 게시물에 연결된 스토리 ID 목록
     */
    private List<Long> storyIds;

    /**
     * 간단한 생성자
     * @param id 게시물 ID
     * @param title 게시물 제목
     * @param startDate 여행 시작 날짜
     * @param endDate 여행 종료 날짜
     * @param location 여행지 위치
     * @param companion 여행 동반자 정보
     */
    public PostDto(Long id, String title, String startDate, String endDate, String location, String companion) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.companion = companion;
    }

    /**
     * Entity(Post)를 DTO(PostDto)로 변환하는 생성자
     * @param post Post 엔티티 객체
     */
    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.startDate = post.getStartDate();
        this.endDate = post.getEndDate();
        this.location = post.getLocation();
        this.companion = post.getCompanion();
        this.storyIds = post.getStories() != null
                ? post.getStories().stream()
                .map(Story::getId)
                .collect(Collectors.toList())
                : List.of();
    }
}
