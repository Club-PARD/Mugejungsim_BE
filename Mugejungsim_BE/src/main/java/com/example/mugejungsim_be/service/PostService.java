package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.Story;
import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.PostRepository;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    public Long createPost(Long userId, Post post) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        post.setUser(user);

        // 초기 상태로 bottle 없이 저장
        return postRepository.save(post).getId();
    }


    public Post finalizePost(Long postId, Post updatedPostData) {
        // 기존 게시물 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 게시물의 기본 정보 업데이트
        if (updatedPostData.getTitle() != null) post.setTitle(updatedPostData.getTitle());
        if (updatedPostData.getStartDate() != null) post.setStartDate(updatedPostData.getStartDate());
        if (updatedPostData.getEndDate() != null) post.setEndDate(updatedPostData.getEndDate());
        if (updatedPostData.getLocation() != null) post.setLocation(updatedPostData.getLocation());
        if (updatedPostData.getCompanion() != null) post.setCompanion(updatedPostData.getCompanion());
        if (updatedPostData.getBottle() != null) post.setBottle(updatedPostData.getBottle());

        // 스토리 업데이트 로직
        if (updatedPostData.getStories() != null) {
            Map<String, Story> existingStoriesMap = post.getStories().stream()
                    .collect(Collectors.toMap(Story::getPid, story -> story)); // 기존 스토리들을 PID 기준으로 매핑

            for (Story updatedStory : updatedPostData.getStories()) {
                Story existingStory = existingStoriesMap.get(updatedStory.getPid());
                if (existingStory != null) {
                    // 기존 스토리 업데이트
                    if (updatedStory.getContent() != null) existingStory.setContent(updatedStory.getContent());
                    if (updatedStory.getCategories() != null) existingStory.setCategories(updatedStory.getCategories());
                    if (updatedStory.getImagePath() != null) existingStory.setImagePath(updatedStory.getImagePath());
                } else {
                    // 새로운 스토리 추가
                    updatedStory.setPost(post); // 새로운 스토리와 게시물 연결
                    post.getStories().add(updatedStory);
                }
            }
        }

        // 게시물 저장
        return postRepository.save(post);
    }


    public List<Post> getPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return postRepository.findByUser(user);
    }


    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .filter(existingPost -> existingPost.getUser().getId().equals(userId)) // 권한 확인
                .orElseThrow(() -> new RuntimeException("Post not found or access denied"));

        postRepository.delete(post);
    }
}
