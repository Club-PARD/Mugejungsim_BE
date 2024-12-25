package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.CustomOAuth2User;
import com.example.mugejungsim_be.dto.PostRequestDto;
import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.service.PostService;
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

    @PostMapping
    public ResponseEntity<?> createPost(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @RequestBody PostRequestDto postRequestDto
    ) {
        if (customUser == null || customUser.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        try {
            Post post = postService.createPost(
                    customUser.getUser(),
                    postRequestDto.getTitle(),
                    postRequestDto.getDescription(),
                    postRequestDto.getStoryIds()
            );
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }




    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam List<Long> storyIds,
            @AuthenticationPrincipal CustomOAuth2User customUser) {

        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            Post post = postService.updatePost(id, title, description, storyIds, customUser.getUser());
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable Long id) {

        if (customUser == null || customUser.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            postService.deletePost(id, customUser.getUser());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(@AuthenticationPrincipal CustomOAuth2User customUser) {
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Post> posts = postService.getPostsByUser(customUser.getUser());
        return ResponseEntity.ok(posts);
    }
}
