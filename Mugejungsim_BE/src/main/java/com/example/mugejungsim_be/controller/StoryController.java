package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.S3Uploader;
import com.example.mugejungsim_be.service.StoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;
    private final S3Uploader s3Uploader;

    /**
     * 스토리 생성
     */
    @Operation(summary = "스토리 생성", description = "특정 게시물에 새로운 스토리를 생성합니다. 사진 업로드 및 JSON 데이터를 포함해야 합니다.")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createStory(
            @RequestParam Long postId,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "data", required = true) String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> requestData = objectMapper.readValue(jsonData, Map.class);

            List<Map<String, Object>> photos = (List<Map<String, Object>>) requestData.get("photos");
            if (photos == null || photos.isEmpty()) {
                return ResponseEntity.badRequest().body("Photos cannot be null or empty");
            }

            Map<String, Object> photoData = photos.get(0);
            String content = (String) photoData.get("content");
            List<String> categories = (List<String>) photoData.get("categories");
            Integer orderIndex = (Integer) photoData.get("orderIndex");
            String imagePathFromData = (String) photoData.get("imagePath");

            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Content cannot be null or empty");
            }

            String imagePath = null;
            if (photo != null && !photo.isEmpty()) {
                imagePath = s3Uploader.upload(photo, "stories");
            } else if (imagePathFromData != null) {
                imagePath = imagePathFromData;
            }

            Long storyId = storyService.createStory(postId, content, categories, imagePath, orderIndex);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Story created successfully");
            response.put("storyId", storyId);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file to S3: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    /**
     * 스토리 업데이트
     */
    @Operation(summary = "스토리 업데이트", description = "특정 스토리의 내용을 업데이트합니다. 이미지 및 카테고리를 수정할 수 있습니다.")
    @PutMapping("/{storyId}")
    public ResponseEntity<?> updateStory(
            @PathVariable Long storyId,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) List<String> categories,
            @RequestPart(required = false) MultipartFile image,
            @RequestParam(required = false) Integer orderIndex) {
        try {
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                imagePath = s3Uploader.upload(image, "stories");
            }

            if (content != null && content.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Content cannot be an empty string.");
            }

            Long updatedStoryId = storyService.updateStory(storyId, content, categories, imagePath, orderIndex);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Story updated successfully");
            response.put("storyId", updatedStoryId);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file to S3: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error updating story: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    /**
     * 스토리 삭제
     */
    @Operation(summary = "스토리 삭제", description = "특정 게시물에서 스토리를 삭제합니다.")
    @DeleteMapping("/{storyId}")
    public ResponseEntity<?> deleteStory(
            @RequestParam Long postId,
            @PathVariable Long storyId) {
        try {
            storyService.deleteStory(postId, storyId);
            return ResponseEntity.ok("Story deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Story not found or access denied");
        }
    }

    /**
     * 특정 게시물의 스토리 목록 조회
     */
    @Operation(summary = "스토리 목록 조회", description = "특정 게시물에 포함된 모든 스토리를 조회합니다.")
    @GetMapping("/{postId}/stories")
    public ResponseEntity<List<StoryDto>> getStoriesForPost(@PathVariable Long postId) {
        List<StoryDto> stories = storyService.getStoriesByPostId(postId);
        return ResponseEntity.ok(stories);
    }

    /**
     * 스토리 순서 업데이트
     */
    @Operation(summary = "스토리 순서 업데이트", description = "특정 게시물의 스토리 순서를 변경합니다.")
    @PutMapping("/{postId}/update-order")
    public ResponseEntity<?> updateStoryOrder(
            @PathVariable Long postId,
            @RequestBody List<Long> storyIds) {
        try {
            storyService.updateStoryOrder(postId, storyIds);
            return ResponseEntity.ok("Story order updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating story order");
        }
    }
}