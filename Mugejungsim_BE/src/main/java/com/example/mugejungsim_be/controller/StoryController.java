package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.S3Uploader;
import com.example.mugejungsim_be.service.StoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @PutMapping("/{storyId}")
    public ResponseEntity<?> updateStory(
            @PathVariable Long storyId,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) List<String> categories,
            @RequestPart(required = false) MultipartFile image,
            @RequestParam(required = false) Integer orderIndex) {
        try {
            // 이미지 처리: S3 업로드
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                imagePath = s3Uploader.upload(image, "stories");
            }

            // 파라미터 검증 및 기본값 설정
            if (content != null && content.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Content cannot be an empty string.");
            }

            // 스토리 업데이트
            Long updatedStoryId = storyService.updateStory(storyId, content, categories, imagePath, orderIndex);

            // 성공 응답
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Story updated successfully");
            response.put("storyId", updatedStoryId);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            // S3 업로드 실패
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file to S3: " + e.getMessage());
        } catch (RuntimeException e) {
            // 기타 런타임 예외 처리
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error updating story: " + e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    /**
     * 스토리 삭제
     */
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
    @GetMapping("/{postId}/stories")
    public ResponseEntity<List<StoryDto>> getStoriesForPost(@PathVariable Long postId) {
        List<StoryDto> stories = storyService.getStoriesByPostId(postId);
        return ResponseEntity.ok(stories);
    }

    /**
     * 스토리 순서 업데이트
     */
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