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

    public Post finalizePost(Long postId, Post updatedPostData) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 기존 필드 업데이트
        post.setTitle(updatedPostData.getTitle());
        post.setStartDate(updatedPostData.getStartDate());
        post.setEndDate(updatedPostData.getEndDate());
        post.setLocation(updatedPostData.getLocation());
        post.setCompanion(updatedPostData.getCompanion());
        post.setBottle(updatedPostData.getBottle());

        // 기존 스토리를 유지하며 업데이트
        if (updatedPostData.getStories() != null) {
            post.getStories().clear(); // 기존 참조를 명시적으로 삭제
            post.getStories().addAll(updatedPostData.getStories());
            updatedPostData.getStories().forEach(story -> story.setPost(post)); // 역참조 설정
        }

        return postRepository.save(post);
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
