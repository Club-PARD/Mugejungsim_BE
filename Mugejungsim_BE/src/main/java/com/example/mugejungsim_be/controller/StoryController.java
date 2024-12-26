package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.CustomOAuth2User;
import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.entity.Story;
import com.example.mugejungsim_be.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @PostMapping
    public ResponseEntity<?> createStory(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @RequestBody StoryDto storyDto) {

        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        try {
            Story story = storyService.createStory(customUser.getUser(), storyDto.getContent(), storyDto.getEmoji());
            return ResponseEntity.ok(story);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Story>> getStories(@AuthenticationPrincipal CustomOAuth2User customUser) {
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Story> stories = storyService.getStoriesByUserId(customUser.getUser().getId());
        return ResponseEntity.ok(stories);
    }
}
