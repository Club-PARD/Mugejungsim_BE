package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 게시물 생성 (초기 단계 - bottle 없이)
     */
    @Operation(
            summary = "게시물 생성",
            description = "사용자 ID를 기반으로 게시물을 생성합니다. 초기 단계에서는 bottle 값 없이 저장됩니다."
    )
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPost(
            @RequestParam Long userId,
            @RequestBody Post post) {
        if (post.getPid() == null || post.getPid().isEmpty()) {
            post.setPid(UUID.randomUUID().toString()); // 고유 pid 자동 생성
        }

        // bottle 없이 초기 저장
        Long postId = postService.createPost(userId, post);

        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId);
        response.put("pid", post.getPid());
        response.put("userId", userId);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "게시물 업데이트 (최종화)",
            description = "게시물 ID를 기반으로 게시물의 스토리와 bottle 정보를 업데이트합니다."
    )
    @PutMapping("/{postId}/finalize")
    public ResponseEntity<Post> finalizePost(
            @PathVariable Long postId,
            @RequestBody Post updatedPostData) {
        Post finalizedPost = postService.finalizePost(postId, updatedPostData);
        return ResponseEntity.ok(finalizedPost);
    }
    /**
     * 게시물 삭제
     */
    @Operation(
            summary = "게시물 삭제",
            description = "특정 사용자의 게시물을 삭제합니다."
    )
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @RequestParam Long userId, // userId를 요청에서 직접 받음
            @PathVariable Long postId) {
        postService.deletePost(userId, postId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 사용자별 게시물 조회
     */
    @Operation(
            summary = "사용자별 게시물 조회",
            description = "특정 사용자의 모든 게시물을 조회합니다."
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId) {
        try {
            // 서비스에서 사용자 ID에 해당하는 게시물 목록을 가져옴
            List<Post> posts = postService.getPostsByUserId(userId);
            return ResponseEntity.ok(posts);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 사용자가 없는 경우
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 기타 예외 발생 시
        }
    }
}