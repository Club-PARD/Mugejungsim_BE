package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.Story;
import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.PostRepository;
import com.example.mugejungsim_be.repository.StoryRepository;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StoryService {
    private static final Logger logger = LoggerFactory.getLogger(StoryService.class);

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;


    @Transactional
    public Long createStory(Long postId, String content, List<String> categories, String imagePath, Integer orderIndex) {
        // Null 체크 및 기본값 확인
        if (postId == null || postId <= 0) {
            throw new IllegalArgumentException("Invalid postId: " + postId);
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content must not be empty");
        }

        // 로그 추가 - 입력값 확인
        logger.info("Creating story with postId: {}, content: {}", postId, content);

        // Story 객체 생성
        Story story = new Story();

        // 게시물 조회 및 설정
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        story.setPost(post);

        // Story 세부 정보 설정
        story.setContent(content);
        story.setCategories(categories != null ? categories : new ArrayList<>());
        story.setImagePath(imagePath);
        story.setPid(UUID.randomUUID().toString()); // Unique pid 생성

        // orderIndex가 전달되지 않은 경우 기본값 설정
        if (orderIndex == null) {
            Integer maxOrderIndex = storyRepository.findMaxOrderIndexByPostId(postId);
            story.setOrderIndex(maxOrderIndex != null ? maxOrderIndex + 1 : 0);
        } else {
            story.setOrderIndex(orderIndex);
        }

        // 로그 추가 - 저장 전 상태 확인
        logger.debug("Saving story with details: {}", story);

        // Story 저장
        Story savedStory = storyRepository.save(story);

        // 로그 추가 - 저장 완료
        logger.info("Story created successfully with ID: {}", savedStory.getId());

        // ID 반환
        return savedStory.getId();
    }

    @Transactional
    public Long updateStory(Long storyId, String content, List<String> categories, String imagePath, Integer orderIndex) {
        // 스토리 조회
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found with ID: " + storyId));

        // 업데이트 필드 설정
        if (content != null) {
            story.setContent(content);
        }

        if (categories != null) {
            story.setCategories(categories);
        }

        if (imagePath != null) {
            story.setImagePath(imagePath);
        }

        if (orderIndex != null) {
            story.setOrderIndex(orderIndex);
        }

        // 변경 사항 저장
        Story updatedStory = storyRepository.save(story);

        // 업데이트된 스토리의 ID 반환
        return updatedStory.getId();
    }


    @Transactional
    public ResponseEntity<String> deleteStory(Long postId, Long storyId) {
        // 스토리 조회 및 소속 포스트 확인
        Story story = storyRepository.findById(storyId)
                .filter(existingStory -> existingStory.getPost().getId().equals(postId))
                .orElseThrow(() -> new RuntimeException("Story not found or access denied"));

        // 삭제할 스토리의 정보 준비
        Long deletedStoryId = story.getId();
        Long deletedPostId = story.getPost().getId();

        // 스토리 삭제
        storyRepository.delete(story);

        // 프론트엔드에 삭제된 스토리 정보를 전송
        String message = String.format("Story with ID %d from Post ID %d has been deleted.", deletedStoryId, deletedPostId);

        // 삭제된 스토리 정보와 함께 성공 메시지 반환
        return ResponseEntity.ok(message);
    }
    @Transactional(readOnly = true)
    public List<StoryDto> getStoriesByPostId(Long postId) {
        return storyRepository.findByPostId(postId).stream()
                .map(StoryDto::new)
                .toList();
    }


    @Transactional
    public void updateStoryOrder(Long postId, List<Long> storyIds) {
        // 주어진 포스트의 모든 스토리 조회
        List<Story> stories = storyRepository.findByPostId(postId);

        if (stories.size() != storyIds.size()) {
            throw new RuntimeException("Mismatch in the number of stories");
        }

        // 스토리 순서 업데이트
        for (int i = 0; i < storyIds.size(); i++) {
            Long storyId = storyIds.get(i);
            Story story = stories.stream()
                    .filter(s -> s.getId().equals(storyId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Story not found with ID: " + storyId));

            story.setOrderIndex(i); // 순서를 i로 설정
        }

        // 변경된 스토리들 저장
        storyRepository.saveAll(stories);
    }
}
