package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.dto.PostDto;
import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.Story;
import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.PostRepository;
import com.example.mugejungsim_be.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시물(Post) 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private StoryRepository storyRepository;

    /**
     * 게시물 생성
     */
    @Transactional
    public PostDto createPost(User user, PostDto postDto) {
        // Post 엔티티 생성
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setStartDate(postDto.getStartDate());
        post.setEndDate(postDto.getEndDate());
        post.setLocation(postDto.getLocation());
        post.setCompanion(postDto.getCompanion());
        post.setUser(user);

        // 스토리와의 연관성 처리 (선택적)
        if (postDto.getStoryIds() != null && !postDto.getStoryIds().isEmpty()) {
            List<Story> stories = storyRepository.findAllById(postDto.getStoryIds());
            post.setStories(stories); // 게시물과 스토리 연결
            stories.forEach(story -> story.setPost(post)); // 각 스토리에 게시물 설정
        }

        // 초기 생성 시 bottle은 null로 저장
        post.setBottle(null);

        // Post 저장
        Post savedPost = postRepository.save(post);

        // 저장된 Post를 DTO로 변환하여 반환
        return convertToDto(savedPost);
    }

    /**
     * 사용자 게시물 조회
     */
    @Transactional(readOnly = true)
    public List<PostDto> getPostsByUserId(Long userId) {
        // 사용자의 게시물 목록 조회 및 DTO 변환
        return postRepository.findByUserId(userId).stream()
                .map(this::convertToDto) // Post -> PostDto 변환
                .collect(Collectors.toList());
    }

    /**
     * 게시물 Bottle 업데이트
     */
    @Transactional
    public PostDto updateBottle(Long userId, Long postId, String bottle) {
        return postRepository.findById(postId)
                .filter(post -> post.getUser().getId().equals(userId)) // 해당 사용자의 게시물인지 확인
                .map(post -> {
                    post.setBottle(bottle); // Bottle 정보 업데이트
                    Post updatedPost = postRepository.save(post);
                    return convertToDto(updatedPost); // 업데이트된 Post를 DTO로 반환
                })
                .orElseThrow(() -> new IllegalArgumentException("Post not found or access denied"));
    }

    /**
     * 게시물 엔티티를 DTO로 변환
     */
    private PostDto convertToDto(Post post) {
        List<Long> storyIds = post.getStories() != null
                ? post.getStories().stream()
                .map(Story::getId)
                .collect(Collectors.toList())
                : List.of();

        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getStartDate(),
                post.getEndDate(),
                post.getLocation(),
                post.getCompanion(),
                post.getBottle(),
                storyIds
        );
    }
}
