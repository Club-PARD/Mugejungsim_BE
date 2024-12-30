package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.service.PostService;
import com.example.mugejungsim_be.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    /**
     * 게시물 생성
     */
    @PostMapping
    public ResponseEntity<Long> createPost(
            @RequestParam Long userId, // userId를 요청에서 직접 받음
            @RequestBody Post post) {
        // pid가 없으면 서버에서 생성 (선택 사항)
        if (post.getPid() == null || post.getPid().isEmpty()) {
            post.setPid(UUID.randomUUID().toString()); // 고유 pid 자동 생성
        }

        Long postId = postService.createPost(userId, post);
        return ResponseEntity.ok(postId);
    }    @GetMapping
    public ResponseEntity<List<Post>> getPostsByUser(@RequestParam Long userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }
    /**
     * 게시물 업데이트
     */
    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @RequestParam Long userId, // userId를 요청에서 직접 받음
            @PathVariable Long postId,
            @RequestBody Post updatedPostData) {
        Post updatedPost = postService.updatePost(userId, postId, updatedPostData);
        return ResponseEntity.ok(updatedPost);
    }

    /**
     * 게시물 삭제
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @RequestParam Long userId, // userId를 요청에서 직접 받음
            @PathVariable Long postId) {
        postService.deletePost(userId, postId);
        return ResponseEntity.noContent().build();
    }
}
