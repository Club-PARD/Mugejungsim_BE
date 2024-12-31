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

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "스토리 생성", description = "사진과 JSON 데이터를 받아 새로운 스토리를 생성합니다.")
    public ResponseEntity<?> createStories(
            @RequestPart(value = "photos") List<MultipartFile> photos,
            @RequestPart(value = "data") String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 데이터 파싱
            List<Map<String, Object>> requestDataList = objectMapper.readValue(jsonData, List.class);

            if (requestDataList.isEmpty()) {
                return ResponseEntity.badRequest().body("Data cannot be empty");
            }

            // 사진 업로드와 JSON 데이터 매핑
            for (int i = 0; i < requestDataList.size(); i++) {
                Map<String, Object> storyData = requestDataList.get(i);

                // 사진 업로드 처리 (순서에 맞춰 매칭)
                if (photos != null && photos.size() > i && !photos.get(i).isEmpty()) {
                    String imagePath = s3Uploader.upload(photos.get(i), "stories");
                    storyData.put("imagePath", imagePath); // 업로드된 경로를 추가
                }
            }

            // 서비스 호출하여 스토리 생성
            List<Long> storyIds = storyService.createStory(requestDataList);

            // 응답 생성
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Stories created successfully");
            response.put("storyIds", storyIds);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }



    @Operation(summary = "스토리 업데이트", description = "특정 스토리의 내용을 업데이트합니다. 이미지 및 카테고리를 수정할 수 있습니다.")
    @PutMapping("/{storyId}")
    public ResponseEntity<?> updateStory(
            @PathVariable Long storyId,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) List<String> categories,
            @RequestPart(required = false) MultipartFile image) {
        try {
            // 이미지 업로드 처리
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                imagePath = s3Uploader.upload(image, "stories");
            }

            // 내용 검증
            if (content != null && content.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Content cannot be an empty string.");
            }

            // 서비스 호출
            Long updatedStoryId = storyService.updateStory(storyId, content, categories, imagePath);

            // 응답 생성
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Story updated successfully");
            response.put("storyId", updatedStoryId);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file to S3: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error updating story: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
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
}
