package com.example.mugejungsim_be.dto;

import com.example.mugejungsim_be.entity.Story;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 추가
public class StoryDto {
    private Long id;
    private String content;
    private String emoji;

    @JsonCreator // Jackson이 사용할 생성자 지정
    public StoryDto(
            @JsonProperty("id") Long id,
            @JsonProperty("content") String content,
            @JsonProperty("emoji") String emoji
    ) {
        this.id = id;
        this.content = content;
        this.emoji = emoji;
    }

    public StoryDto(Story story) {
    }
}
