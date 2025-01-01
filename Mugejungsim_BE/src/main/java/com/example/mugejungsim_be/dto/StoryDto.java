package com.example.mugejungsim_be.dto;

import com.example.mugejungsim_be.entity.Story;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 스토리 데이터를 전달하기 위한 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryDto {

    private Long id;
    private String content;
    private List<String> categories; // 다중 카테고리 필드
    private String imagePath; // 이미지 경로 추가
    private String pid;

    public StoryDto(Story story) {
        this.id = story.getId();
        this.content = story.getContent();
        this.categories = story.getCategories() != null ? story.getCategories() : List.of(); // 기본값 설정
        this.imagePath = story.getImagePath() != null ? story.getImagePath() : ""; // 기본값 설정
        this.pid = story.getPid();
    }
}

