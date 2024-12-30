package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.service.FileStorageService;
import com.example.mugejungsim_be.service.StoryService;
import com.example.mugejungsim_be.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createStory(
            @RequestParam Long userId,
            @RequestParam Long postId,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "data", required = true) String jsonData) {

        try {
            // JSON 데이터를 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> requestData = objectMapper.readValue(jsonData, Map.class);

            // "photos" 배열 가져오기
            List<Map<String, Object>> photos = (List<Map<String, Object>>) requestData.get("photos");
            if (photos == null || photos.isEmpty()) {
                return ResponseEntity.badRequest().body("Photos cannot be null or empty");
            }

            // 첫 번째 사진 데이터만 처리
            Map<String, Object> photoData = photos.get(0);
            String content = (String) photoData.get("content");
            List<String> categories = (List<String>) photoData.get("categories");
            Integer orderIndex = (Integer) photoData.get("orderIndex");
            String imagePathFromData = (String) photoData.get("imagePath");

            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Content cannot be null or empty");
            }

            // 사진 처리
            String imagePath = null;
            if (photo != null && !photo.isEmpty()) {
                imagePath = fileStorageService.storeImage(photo);
            } else if (imagePathFromData != null) {
                imagePath = imagePathFromData;
            }

            // 스토리 생성
            storyService.createStory(userId, postId, content, categories, imagePath, orderIndex);

            return ResponseEntity.ok("Story created successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }


    @PutMapping("/{storyId}")
    public ResponseEntity<StoryDto> updateStory(
            @RequestParam Long userId,
            @PathVariable Long storyId,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) List<String> categories,
            @RequestPart(required = false) MultipartFile image,
            @RequestParam(required = false) Integer orderIndex) {

        try {
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                imagePath = fileStorageService.storeImage(image);
            }

            StoryDto updatedStory = storyService.updateStory(storyId, userService.getUserById(userId), content, categories, imagePath, orderIndex);
            return ResponseEntity.ok(updatedStory);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{storyId}")
    public ResponseEntity<?> deleteStory(
            @RequestParam Long userId,
            @PathVariable Long storyId) {
        try {
            storyService.deleteStory(storyId, userId);
            return ResponseEntity.ok("Story deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Story not found or access denied");
        }
    }

    @GetMapping("/{postId}/stories")
    public ResponseEntity<List<StoryDto>> getStoriesForPost(@PathVariable Long postId) {
        List<StoryDto> stories = storyService.getStoriesByPostId(postId);
        return ResponseEntity.ok(stories);
    }
}
