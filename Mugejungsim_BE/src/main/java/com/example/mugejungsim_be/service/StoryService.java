package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.Story;
import com.example.mugejungsim_be.repository.PostRepository;
import com.example.mugejungsim_be.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private PostRepository postRepository;

    /**
     * 스토리 생성 (단일/다중 처리)
     */
    @Transactional
    public List<Long> createStory(List<Map<String, Object>> storiesData) {
        List<Long> storyIds = new ArrayList<>();

        for (Map<String, Object> data : storiesData) {
            Long postId = Long.valueOf((Integer) data.get("postId"));
            String content = (String) data.get("content");
            List<String> categories = (List<String>) data.get("categories");
            String imagePath = (String) data.get("imagePath");
            String pid = (String) data.get("pid");

            // pid 유효성 검사
            if (pid == null || pid.trim().isEmpty()) {
                throw new IllegalArgumentException("PID must not be empty");
            }

            Post post = validatePost(postId);

            Story story = new Story();
            story.setPost(post);
            story.setContent(content);
            story.setCategories(categories != null ? categories : new ArrayList<>());
            story.setImagePath(imagePath);
            story.setPid(pid); // 프론트엔드에서 받은 PID 사용

            storyIds.add(storyRepository.save(story).getId());
        }

        return storyIds;
    }

    /**
     * 스토리 업데이트
     */
    @Transactional
    public Long updateStory(Long storyId, String content, List<String> categories, String imagePath) {
        // 스토리 존재 여부 확인
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found with ID: " + storyId));

        // 내용 업데이트 (유효성 검증 포함)
        if (content != null) {
            if (content.trim().isEmpty()) {
                throw new IllegalArgumentException("Content cannot be an empty string.");
            }
            story.setContent(content);
        }

        // 카테고리 업데이트
        if (categories != null) {
            story.setCategories(categories);
        }

        // 이미지 경로 업데이트
        if (imagePath != null) {
            story.setImagePath(imagePath);
        }

        // 변경 사항 저장
        return storyRepository.save(story).getId();
    }

    /**
     * 특정 게시물의 모든 스토리 조회
     */
    @Transactional(readOnly = true)
    public List<StoryDto> getStoriesByPostId(Long postId) {
        return storyRepository.findByPostId(postId).stream()
                .map(StoryDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 스토리 삭제
     */
    @Transactional
    public void deleteStory(Long postId, Long storyId) {
        Story story = storyRepository.findById(storyId)
                .filter(existingStory -> existingStory.getPost().getId().equals(postId))
                .orElseThrow(() -> new RuntimeException("Story not found or does not belong to the specified post"));

        storyRepository.delete(story);
    }
    /**
     * 유효성 검사 - 게시물
     */
    private Post validatePost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
    }

    /**
     * 유효성 검사 - 내용
     */
    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content must not be empty");
        }
    }
}
