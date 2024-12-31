package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.PostRepository;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 새로운 게시물 생성
     */
    public Long createPost(Long userId, Post post) {
        // userId로 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // 게시물에 사용자 설정
        post.setUser(user);

        // 게시물 저장
        Post savedPost = postRepository.save(post);

        // 생성된 게시물 ID 반환
        return savedPost.getId();
    }

    /**
     * 사용자의 모든 게시물 조회
     */

    public List<Post> getPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return postRepository.findByUser(user);
    }

    /**
     * 게시물 업데이트
     */
    public Post updatePost(Long userId, Long postId, Post updatedPostData) {
        Post post = postRepository.findById(postId)
                .filter(existingPost -> existingPost.getUser().getId().equals(userId)) // 권한 확인
                .orElseThrow(() -> new RuntimeException("Post not found or access denied"));

        post.setTitle(updatedPostData.getTitle());
        post.setStartDate(updatedPostData.getStartDate());
        post.setEndDate(updatedPostData.getEndDate());
        post.setLocation(updatedPostData.getLocation());
        post.setCompanion(updatedPostData.getCompanion());
        post.setBottle(updatedPostData.getBottle());

        return postRepository.save(post);
    }

    /**
     * 게시물 삭제
     */
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .filter(existingPost -> existingPost.getUser().getId().equals(userId)) // 권한 확인
                .orElseThrow(() -> new RuntimeException("Post not found or access denied"));

        postRepository.delete(post);
    }
}
