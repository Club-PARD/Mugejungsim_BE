package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.Story;
import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.PostRepository;
import com.example.mugejungsim_be.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private PostRepository postRepository;

    /**
     * 새로운 스토리 생성
     */
    @Transactional
    public StoryDto createStory(User user, String content, List<String> categories, String imagePath, Integer orderIndex, Long postId) {
        Story story = new Story();
        story.setUser(user);
        story.setContent(content);
        story.setCategories(categories); // 다중 카테고리 설정
        story.setImagePath(imagePath);

        if (postId != null) {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
            story.setPost(post);

            Integer maxOrderIndex = storyRepository.findMaxOrderIndexByPostId(postId);
            story.setOrderIndex(maxOrderIndex != null ? maxOrderIndex + 1 : 0);
        } else {
            story.setOrderIndex(orderIndex != null ? orderIndex : 0);
        }

        Story savedStory = storyRepository.save(story);
        return new StoryDto(savedStory);
    }

    /**
     * 스토리 수정
     */
    @Transactional
    public StoryDto updateStory(Long storyId, User user, String content, List<String> categories, String imagePath, Integer orderIndex) {
        Story story = storyRepository.findById(storyId)
                .filter(existingStory -> existingStory.getUser().getId().equals(user.getId())) // 사용자 권한 확인
                .orElseThrow(() -> new RuntimeException("Story not found or access denied"));

        // 수정 가능한 필드 업데이트
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

    /**
     * 스토리 삭제
     */
    @Transactional
    public void deleteStory(Long storyId, User user) {
        Story story = storyRepository.findById(storyId)
                .filter(existingStory -> existingStory.getUser().getId().equals(user.getId())) // 사용자 권한 확인
                .orElseThrow(() -> new RuntimeException("Story not found or access denied"));

        storyRepository.delete(story);
    }

    /**
     * 특정 게시물에 연결된 스토리 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Story> getStoriesByPostId(Long postId) {
        return storyRepository.findByPostId(postId);
    }
}
