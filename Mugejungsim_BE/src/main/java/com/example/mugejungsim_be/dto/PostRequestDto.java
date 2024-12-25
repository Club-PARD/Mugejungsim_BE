package com.example.mugejungsim_be.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String description;
    private List<Long> storyIds;
}