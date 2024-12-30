package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.Story;
import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.PostRepository;
import com.example.mugejungsim_be.repository.StoryRepository;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;



    @Transactional
    public StoryDto createStory(Long userId, Long postId, String content, List<String> categories, String imagePath, Integer orderIndex) {
        Story story = new Story();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        story.setUser(user);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        story.setPost(post);

        story.setContent(content);
        story.setCategories(categories);
        story.setImagePath(imagePath);
        story.setPid(UUID.randomUUID().toString()); // Unique pid 생성

        // orderIndex가 전달되지 않은 경우 기본값 설정
        if (orderIndex == null) {
            Integer maxOrderIndex = storyRepository.findMaxOrderIndexByPostId(postId);
            story.setOrderIndex(maxOrderIndex != null ? maxOrderIndex + 1 : 0);
        } else {
            story.setOrderIndex(orderIndex);
        }

        Story savedStory = storyRepository.save(story);
        return new StoryDto(savedStory);
    }

    @Transactional
    public StoryDto updateStory(Long storyId, User user, String content, List<String> categories, String imagePath, Integer orderIndex) {
        Story story = storyRepository.findById(storyId)
                .filter(existingStory -> existingStory.getUser().getId().equals(user.getId())) // 사용자 권한 확인
                .orElseThrow(() -> new RuntimeException("Story not found or access denied"));

        if (content != null) {
            story.setContent(content);
        }
        if (categories != null && !categories.isEmpty()) {
            story.setCategories(categories);
        }
        if (imagePath != null) {
            story.setImagePath(imagePath);
        }
        if (orderIndex != null) {
            story.setOrderIndex(orderIndex);
        }

        Story updatedStory = storyRepository.save(story);
        return new StoryDto(updatedStory);
    }

    @Transactional
    public void deleteStory(Long storyId, Long userId) {
        Story story = storyRepository.findById(storyId)
                .filter(existingStory -> existingStory.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Story not found or access denied"));

        storyRepository.delete(story);
    }

    @Transactional(readOnly = true)
    public List<StoryDto> getStoriesByPostId(Long postId) {
        return storyRepository.findByPostId(postId).stream()
                .map(StoryDto::new)
                .toList();
    }
}
