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
     * 새로운 게시물 생성 (bottle 없이 초기 단계)
     */
    public Long createPost(Long userId, Post post) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        post.setUser(user);

        // 초기 상태로 bottle 없이 저장
        return postRepository.save(post).getId();
    }

    /**
     * 게시물 최종화 (스토리와 bottle 업데이트)
     */
    public Post finalizePost(Long userId, Long postId, Post updatedPostData) {
        Post existingPost = postRepository.findById(postId)
                .filter(post -> post.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Post not found or access denied"));

        // bottle 및 스토리 업데이트
        if (updatedPostData.getBottle() != null) {
            existingPost.setBottle(updatedPostData.getBottle());
        }
        if (updatedPostData.getStories() != null) {
            existingPost.setStories(updatedPostData.getStories());
        }

        return postRepository.save(existingPost);
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
