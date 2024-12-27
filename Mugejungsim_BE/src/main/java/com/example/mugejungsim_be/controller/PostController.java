package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.CustomOAuth2User;
import com.example.mugejungsim_be.dto.PostDto;
import com.example.mugejungsim_be.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 새로운 게시물 생성
     *
     * @param customUser 인증된 사용자 정보 (OAuth2 토큰에서 가져옴)
     * @param postDto    생성할 게시물 데이터
     * @return 생성된 게시물 정보
     */
    @Operation(
            summary = "게시물 생성",
            description = "인증된 사용자가 새로운 게시물을 생성합니다. 유효한 OAuth2 토큰이 필요합니다.",
            tags = { "게시물 관리" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물이 성공적으로 생성됨", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "400", description = "입력값이 잘못되었거나 필수 데이터가 누락됨"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 접근")
    })
    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @RequestBody PostDto postDto) {
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (postDto.getDescription() == null || postDto.getDescription().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 설명이 없을 때 오류 반환
        }

        PostDto createdPost = postService.createPost(customUser.getUser(), postDto);
        return ResponseEntity.ok(createdPost);
    }

    /**
     * 사용자 게시물 조회
     *
     * @param customUser 인증된 사용자 정보 (OAuth2 토큰에서 가져옴)
     * @return 사용자가 생성한 게시물 목록
     */
    @Operation(
            summary = "사용자 게시물 조회",
            description = "인증된 사용자가 생성한 모든 게시물을 조회합니다. 유효한 OAuth2 토큰이 필요합니다.",
            tags = { "게시물 관리" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물이 성공적으로 조회됨", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 접근")
    })
    @GetMapping
    public ResponseEntity<List<PostDto>> getPosts(@AuthenticationPrincipal CustomOAuth2User customUser) {
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<PostDto> posts = postService.getPostsByUserId(customUser.getUser().getId());
        return ResponseEntity.ok(posts);
    }

}
