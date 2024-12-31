package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.service.PostService;
import com.example.mugejungsim_be.service.UserService;
import jakarta.servlet.http.HttpSession;
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
     * 게시물 생성
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPost(
            @RequestParam Long userId, // userId를 요청에서 직접 받음
            @RequestBody Post post) {
        // pid가 없으면 서버에서 생성 (선택 사항)
        if (post.getPid() == null || post.getPid().isEmpty()) {
            post.setPid(UUID.randomUUID().toString()); // 고유 pid 자동 생성
        }

        // 서비스 호출
        Long postId = postService.createPost(userId, post);

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId); // 생성된 postId 반환
        response.put("pid", post.getPid()); // 고유 pid 포함
        response.put("userId", userId); // 요청한 userId 포함

        return ResponseEntity.ok(response);
    }


    /**
     * 게시물 업데이트
     */

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @RequestParam Long userId, // 수정하려는 사용자 ID
            @PathVariable Long postId, // 수정하려는 게시물 ID
            @RequestBody Post updatedPostData // 수정된 데이터
    ) {
        // 사용자와 postId로 게시물 업데이트
        Post updatedPost = postService.updatePost(userId, postId, updatedPostData);
        return ResponseEntity.ok(updatedPost); // 수정된 게시물 반환
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
