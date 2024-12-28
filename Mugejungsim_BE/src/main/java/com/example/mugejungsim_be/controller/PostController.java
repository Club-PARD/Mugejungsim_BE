package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.CustomOAuth2User;
import com.example.mugejungsim_be.dto.PostDto;
import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.service.PostService;
import com.example.mugejungsim_be.service.StoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post API", description = "게시물 생성 및 병 업데이트 API")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private StoryService storyService;

    /**
     * 게시물 생성
     *
     * @param customUser 인증된 사용자 정보 (OAuth2 토큰에서 가져옴)
     * @param postDto    생성할 게시물 데이터
     * @return 생성된 게시물 정보 (postId 포함)
     */
    @Operation(
            summary = "게시물 생성",
            description = "사용자 인증 후 새로운 게시물을 생성합니다. 병 정보(bottle)는 처음 생성 시 null로 설정됩니다.",
            tags = { "Post API" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "게시물 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostDto.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "id": 101,
                                      "title": "My Trip to Paris",
                                      "startDate": "2023-12-01",
                                      "endDate": "2023-12-10",
                                      "location": "Paris",
                                      "companion": "Family",
                                      "bottle": null,
                                      "storyIds": []
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 입력값"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
    })
    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @RequestBody PostDto postDto) {
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PostDto createdPost = postService.createPost(customUser.getUser(), postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    /**
     * 병 정보 업데이트
     *
     * @param customUser 인증된 사용자 정보 (OAuth2 토큰에서 가져옴)
     * @param postId     병 정보를 업데이트할 게시물 ID
     * @param bottle     업데이트할 병 정보
     * @return 업데이트된 게시물 정보
     */
    @Operation(
            summary = "병 정보 업데이트",
            description = "기존 게시물에 병 정보(bottle)를 업데이트합니다.",
            tags = { "Post API" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "병 정보 업데이트 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
    })
    @PatchMapping("/{postId}/bottle")
    public ResponseEntity<PostDto> updateBottle(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable Long postId,
            @RequestBody String bottle) {
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PostDto updatedPost = postService.updateBottle(customUser.getUser().getId(), postId, bottle);
        return ResponseEntity.ok(updatedPost);
    }

    /**
     * 사용자 게시물 조회
     *
     * @param customUser 인증된 사용자 정보 (OAuth2 토큰에서 가져옴)
     * @return 사용자가 생성한 게시물 목록
     */
    @Operation(
            summary = "사용자 게시물 조회",
            description = "사용자 인증 후 사용자가 생성한 모든 게시물을 조회합니다.",
            tags = { "Post API" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "게시물 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostDto.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
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
