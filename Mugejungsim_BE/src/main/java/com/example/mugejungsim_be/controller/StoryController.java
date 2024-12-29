package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.CustomOAuth2User;
import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.entity.Story;
import com.example.mugejungsim_be.service.FileStorageService;
import com.example.mugejungsim_be.service.StoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${file.base-url}")
    private String baseUrl;

    /**
     * 새로운 스토리 생성
     */
    @Operation(summary = "Create a new story", description = "Create a new story with optional categories, image, and orderIndex.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Story created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping
    public ResponseEntity<StoryDto> createStory(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @RequestParam("content") String content,
            @RequestParam("categories") List<String> categories,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "orderIndex", required = false) Integer orderIndex,
            @RequestParam(value = "postId", required = false) Long postId) {

        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            try {
                // Store the image and get its public URL
                String storedFilename = fileStorageService.storeImage(image);
                imagePath = baseUrl + "/" + storedFilename;
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }

        StoryDto createdStory = storyService.createStory(
                customUser.getUser(),
                content,
                categories,
                imagePath,
                orderIndex,
                postId
        );

        return ResponseEntity.ok(createdStory);
    }

    /**
     * 스토리 수정
     */
    @Operation(summary = "Update a story", description = "Update an existing story's content, categories, image, or orderIndex.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Story updated successfully"),
            @ApiResponse(responseCode = "404", description = "Story not found or access denied")
    })
    @PutMapping("/{storyId}")
    public ResponseEntity<StoryDto> updateStory(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable Long storyId,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "categories", required = false) List<String> categories,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "orderIndex", required = false) Integer orderIndex) {

        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            try {
                // Store the image and get its public URL
                String storedFilename = fileStorageService.storeImage(image);
                imagePath = baseUrl + "/" + storedFilename;
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }

        StoryDto updatedStory = storyService.updateStory(
                storyId,
                customUser.getUser(),
                content,
                categories,
                imagePath,
                orderIndex
        );

        return ResponseEntity.ok(updatedStory);
    }

    /**
     * 스토리 삭제
     */
    @Operation(summary = "Delete a story", description = "Delete an existing story by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Story deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Story not found or access denied")
    })
    @DeleteMapping("/{storyId}")
    public ResponseEntity<?> deleteStory(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable Long storyId) {

        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        storyService.deleteStory(storyId, customUser.getUser());
        return ResponseEntity.ok("Story deleted successfully");
    }

    /**
     * 특정 게시물에 연결된 스토리 목록 조회
     */
    @Operation(summary = "Get stories for a post", description = "Retrieve all stories linked to a specific post, sorted by orderIndex.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stories retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{postId}/stories")
    public ResponseEntity<List<StoryDto>> getStoriesForPost(@PathVariable Long postId) {
        List<Story> stories = storyService.getStoriesByPostId(postId);
        List<StoryDto> storyDtos = stories.stream()
                .sorted((s1, s2) -> s1.getOrderIndex().compareTo(s2.getOrderIndex()))
                .map(StoryDto::new)
                .toList();
        return ResponseEntity.ok(storyDtos);
    }
}
