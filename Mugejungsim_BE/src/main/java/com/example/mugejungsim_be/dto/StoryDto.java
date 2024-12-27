package com.example.mugejungsim_be.dto;

import com.example.mugejungsim_be.entity.Story;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 스토리 데이터를 전달하기 위한 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryDto {

    /**
     * 스토리 ID
     */
    private Long id;

    /**
     * 스토리 내용
     */
    private String content;

    /**
     * 스토리 카테고리
     */
    private String category;

    /**
     * 스토리 이미지 경로
     */
    private String imagePath;

    /**
     * 스토리 순서 (정렬을 위한 인덱스)
     */
    private Integer orderIndex;

    /**
     * Entity(Story)를 DTO(StoryDto)로 변환하는 생성자
     * @param story Story 엔티티 객체
     */
    public StoryDto(Story story) {
        this.id = story.getId();
        this.content = story.getContent();
        this.category = story.getCategory();
        this.imagePath = story.getImagePath();
        this.orderIndex = story.getOrderIndex(); // 순서를 그대로 복사
    }
}
