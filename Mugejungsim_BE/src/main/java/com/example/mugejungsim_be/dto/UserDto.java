package com.example.mugejungsim_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 사용자 데이터를 전달하기 위한 DTO 클래스
 */
@Getter
@Setter
@Data
@AllArgsConstructor
public class UserDto {

    /**
     * 사용자 ID
     */
    private Long id;

    /**
     * 사용자 이름
     */
    private String name;

    /**
     * 사용자가 작성한 게시물 목록
     */
    private List<PostDto> posts;
}
