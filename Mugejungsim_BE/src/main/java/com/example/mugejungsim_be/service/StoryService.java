package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.Story;
import com.example.mugejungsim_be.repository.PostRepository;
import com.example.mugejungsim_be.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private PostRepository postRepository;

    /**
     * 스토리 생성
     */
    @Transactional
    public Long createStory(Long postId, String content, List<String> categories, String imagePath) {
        // Validate postId and content
        if (postId == null || postId <= 0) {
            throw new IllegalArgumentException("Invalid postId: " + postId);
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content must not be empty");
        }

        // Find the associated post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // Create a new story
        Story story = new Story();
        story.setPost(post);
        story.setContent(content);
        story.setCategories(categories != null ? categories : new ArrayList<>());
        story.setImagePath(imagePath);
        story.setPid(UUID.randomUUID().toString()); // Generate a unique PID

        // Save the story
        Story savedStory = storyRepository.save(story);
        return savedStory.getId();
    }

    /**
     * 스토리 업데이트
     */
    @Transactional
    public Long updateStory(Long storyId, String content, List<String> categories, String imagePath) {
        // Find the story
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found with ID: " + storyId));

        // Update fields if provided
        if (content != null) {
            story.setContent(content);
        }
        if (categories != null) {
            story.setCategories(categories);
        }
        if (imagePath != null) {
            story.setImagePath(imagePath);
        }

        // Save the updated story
        Story updatedStory = storyRepository.save(story);
        return updatedStory.getId();
    }

    /**
     * 특정 게시물의 모든 스토리 조회
     */
    @Transactional(readOnly = true)
    public List<StoryDto> getStoriesByPostId(Long postId) {
        return storyRepository.findByPostId(postId).stream()
                .map(StoryDto::new)
                .toList();
    }

    /**
     * 스토리 삭제
     */
    @Transactional
    public void deleteStory(Long postId, Long storyId) {
        // Find the story and ensure it belongs to the specified post
        Story story = storyRepository.findById(storyId)
                .filter(existingStory -> existingStory.getPost().getId().equals(postId))
                .orElseThrow(() -> new RuntimeException("Story not found or does not belong to the specified post"));

        // Delete the story
        storyRepository.delete(story);
    }
}