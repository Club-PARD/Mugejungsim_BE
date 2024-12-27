package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.CustomOAuth2User;
import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.entity.Story;
import com.example.mugejungsim_be.service.FileStorageService;
import com.example.mugejungsim_be.service.StoryService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 새로운 스토리 생성
     *
     * @param customUser 인증된 사용자 정보 (OAuth2 토큰에서 가져옴)
     * @param content    스토리 내용
     * @param category   스토리 카테고리
     * @param image      업로드할 이미지 파일 (선택 사항)
     * @param postId     연결할 게시물 ID (선택 사항)
     * @return 생성된 스토리 정보
     */
    @Operation(
            summary = "스토리 생성",
            description = "인증된 사용자가 새로운 스토리를 생성합니다. 이미지를 업로드할 수 있으며, 특정 게시물(postId)와 연결할 수 있습니다.",
            tags = { "스토리 관리" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스토리가 성공적으로 생성됨", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoryDto.class))),
            @ApiResponse(responseCode = "400", description = "입력값이 잘못되었거나 필수 데이터가 누락됨"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 접근")
    })
    @PostMapping
    public ResponseEntity<StoryDto> createStory(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @RequestParam("content") String content,
            @RequestParam("category") String category,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "postId", required = false) Long postId // Post ID 전달
    ) throws IOException {

        String imagePath = image != null ? fileStorageService.storeImage(image) : null;

        StoryDto createdStory = storyService.createStory(
                customUser.getUser(),
                content,
                category,
                imagePath,
                null,
                postId
        );

        return ResponseEntity.ok(createdStory);
    }

    /**
     * 특정 게시물에 연결된 스토리 조회
     *
     * @param postId 조회할 게시물의 ID
     * @return 해당 게시물에 연결된 스토리 목록
     */
    @Operation(
            summary = "게시물 스토리 조회",
            description = "특정 게시물에 연결된 모든 스토리를 조회합니다. 스토리는 순서(orderIndex)에 따라 정렬됩니다.",
            tags = { "스토리 관리" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스토리가 성공적으로 조회됨", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoryDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 게시물 ID"),
            @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음")
    })
    @GetMapping("/{postId}/stories")
    public ResponseEntity<List<StoryDto>> getStoriesForPost(@PathVariable Long postId) {
        List<Story> stories = storyService.getStoriesByPostId(postId);
        List<StoryDto> storyDtos = stories.stream()
                .sorted(Comparator.comparing(Story::getOrderIndex)) // 순서 정렬 추가
                .map(StoryDto::new)
                .toList();
        return ResponseEntity.ok(storyDtos);
    }

    /**
     * 스토리 순서 재정렬
     *
     * @param postId 정렬할 게시물의 ID
     * @param body   새로운 스토리 순서를 포함한 요청 데이터
     * @return 성공 또는 실패 메시지
     */
    @Operation(
            summary = "스토리 순서 재정렬",
            description = "특정 게시물에 연결된 스토리들의 순서를 재정렬합니다. 클라이언트는 스토리 ID와 새로운 순서(orderIndex)를 포함한 데이터를 전송해야 합니다.",
            tags = { "스토리 관리" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스토리 순서가 성공적으로 변경됨", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Stories reordered successfully\"}"))),
            @ApiResponse(responseCode = "400", description = "잘못된 입력값"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/{postId}/reorder")
    public ResponseEntity<?> reorderStories(
            @PathVariable Long postId,
            @RequestBody Map<String, List<Map<String, Object>>> body) {

        List<Map<String, Object>> stories = body.get("stories");

        if (stories == null || stories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid or empty stories payload"));
        }

        try {
            List<StoryDto> orderedStories = stories.stream()
                    .map(story -> {
                        Long id = story.get("id") != null ? Long.valueOf(story.get("id").toString()) : null;
                        Integer orderIndex = story.get("orderIndex") != null
                                ? Integer.valueOf(story.get("orderIndex").toString())
                                : null;

                        if (id == null || orderIndex == null) {
                            throw new IllegalArgumentException("Missing id or orderIndex in story payload");
                        }

                        return new StoryDto(id, null, null, null, orderIndex);
                    })
                    .toList();

            storyService.reorderStories(postId, orderedStories);
            return ResponseEntity.ok(Map.of("message", "Stories reordered successfully"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

}
