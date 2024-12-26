package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.CustomOAuth2User;
import com.example.mugejungsim_be.dto.PostDto;
import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.entity.User;
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
    public ResponseEntity<PostDto> createPost(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @RequestBody PostDto postDto) {
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PostDto createdPost = postService.createPost(customUser.getUser(), postDto);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping
    public ResponseEntity<?> getPosts(@AuthenticationPrincipal CustomOAuth2User customUser) {
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        User user = customUser.getUser();
        List<PostDto> posts = postService.getPostsByUserId(user.getId());
        return ResponseEntity.ok(posts);
    }
    @GetMapping("/{postId}/stories")
    public ResponseEntity<List<StoryDto>> getStoriesByPost(@PathVariable Long postId) {
        List<StoryDto> stories = postService.getStoriesByPostId(postId);
        return ResponseEntity.ok(stories);
    }

}
