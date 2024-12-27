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

/**
 * 스토리(Story) 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private PostRepository postRepository;

    /**
     * 새로운 스토리를 생성합니다.
     *
     * @param user       스토리를 생성하는 사용자
     * @param content    스토리 내용
     * @param category   스토리 카테고리
     * @param imagePath  스토리 이미지 경로
     * @param orderIndex 스토리 순서 인덱스 (선택 사항)
     * @param postId     연결된 게시물 ID (선택 사항)
     * @return 생성된 스토리의 DTO
     */
    @Transactional
    public StoryDto createStory(User user, String content, String category, String imagePath, Integer orderIndex, Long postId) {
        Story story = new Story();
        story.setUser(user);
        story.setContent(content);
        story.setCategory(category);
        story.setImagePath(imagePath);

        // 게시물과 연결된 경우 orderIndex 계산
        if (postId != null) {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
            story.setPost(post);

            // 게시물에 연결된 스토리 중 가장 큰 orderIndex를 찾아 +1
            Integer maxOrderIndex = storyRepository.findMaxOrderIndexByPostId(postId);
            story.setOrderIndex(maxOrderIndex != null ? maxOrderIndex + 1 : 0);
        } else {
            // orderIndex가 없으면 기본값 설정
            story.setOrderIndex(orderIndex != null ? orderIndex : 0);
        }

        // 스토리 저장
        Story savedStory = storyRepository.save(story);

        // 디버그 메시지 (필요 시 제거 가능)
        System.out.println("Saved Story with Order Index: " + savedStory.getOrderIndex());

        return new StoryDto(savedStory);
    }

    /**
     * 게시물에 연결된 스토리들의 순서를 재정렬합니다.
     *
     * @param postId        게시물 ID
     * @param orderedStories 순서를 업데이트할 스토리의 DTO 목록
     */
    @Transactional
    public void reorderStories(Long postId, List<StoryDto> orderedStories) {
        List<Story> stories = storyRepository.findByPostId(postId);

        for (StoryDto storyDto : orderedStories) {
            Story story = stories.stream()
                    .filter(s -> s.getId().equals(storyDto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Story not found: " + storyDto.getId()));

            // 클라이언트에서 받은 순서로 업데이트
            story.setOrderIndex(storyDto.getOrderIndex());
        }

        // 변경된 스토리 목록 저장
        storyRepository.saveAll(stories);
    }

    /**
     * 특정 게시물에 연결된 스토리 목록을 조회합니다.
     *
     * @param postId 게시물 ID
     * @return 게시물에 연결된 스토리 목록
     */
    @Transactional(readOnly = true)
    public List<Story> getStoriesByPostId(Long postId) {
        return storyRepository.findByPostId(postId);
    }
}
