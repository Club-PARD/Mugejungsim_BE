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
     * 게시물의 병 정보
     */
    private String bottle;

    /**
     * 게시물에 연결된 스토리 ID 목록
     */
    private List<Long> storyIds;

    /**
     * Entity(Post)를 DTO(PostDto)로 변환하는 생성자
     *
     * @param post Post 엔티티 객체
     */
    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.startDate = post.getStartDate();
        this.endDate = post.getEndDate();
        this.location = post.getLocation();
        this.companion = post.getCompanion();
        this.bottle = post.getBottle();
        this.storyIds = post.getStories() != null
                ? post.getStories().stream()
                .map(Story::getId)
                .collect(Collectors.toList())
                : List.of();
    }
}
