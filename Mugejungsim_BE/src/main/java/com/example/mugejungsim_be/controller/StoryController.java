package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.CustomOAuth2User;
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
    public ResponseEntity<Story> createStory(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @RequestParam String content,
            @RequestParam String emoji) {

        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Story story = storyService.createStory(customUser.getUser(), content, emoji);
        return ResponseEntity.ok(story);
    }

    @GetMapping
    public ResponseEntity<List<Story>> getStories(@AuthenticationPrincipal CustomOAuth2User customUser) {
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Story> stories = storyService.getStoriesByUserId(customUser.getUser().getId());
        return ResponseEntity.ok(stories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Story> updateStory(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @RequestParam String content,
            @RequestParam String emoji) {

        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Story updatedStory = storyService.updateStory(id, customUser.getUser(), content, emoji);
        if (updatedStory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(updatedStory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomOAuth2User customUser) {

        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean isDeleted = storyService.deleteStory(id, customUser.getUser());
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.noContent().build();
    }
}
