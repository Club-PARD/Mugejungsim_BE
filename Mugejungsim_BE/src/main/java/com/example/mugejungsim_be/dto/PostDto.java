package com.example.mugejungsim_be.dto;

import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.Story;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
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

    private Long id; // Long 타입으로 유지
    private String title;
    private String startDate;
    private String endDate;
    private String location;
    private String companion;
    private String bottle;
    private List<Long> storyIds;

    public PostDto(Post post) {
        this.id = post.getId(); // Long 타입 유지
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
                : Collections.emptyList();
    }
}
