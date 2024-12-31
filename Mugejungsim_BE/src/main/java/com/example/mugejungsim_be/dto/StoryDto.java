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


    public StoryDto(Story story) {
        this.id = story.getId();
        this.content = story.getContent();
        this.categories = story.getCategories(); // 다중 카테고리 변환
    }
}

