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
     * 새로운 게시물을 생성합니다.
     *
     * @param user    게시물을 생성하는 사용자
     * @param postDto 게시물 데이터
     * @return 생성된 게시물의 DTO
     */
    @Transactional
    public PostDto createPost(User user, PostDto postDto) {
        // 새로운 Post 엔티티 생성
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(
                postDto.getDescription() != null && !postDto.getDescription().isEmpty()
                        ? postDto.getDescription()
                        : "No description provided"); // 기본값 설정
        post.setStartDate(postDto.getStartDate());
        post.setEndDate(postDto.getEndDate());
        post.setLocation(postDto.getLocation());
        post.setCompanion(postDto.getCompanion());
        post.setUser(user);

        // 스토리 ID가 있는 경우 해당 스토리를 게시물과 연결
        if (postDto.getStoryIds() != null && !postDto.getStoryIds().isEmpty()) {
            List<Story> stories = storyRepository.findAllById(postDto.getStoryIds());
            post.setStories(stories);
            for (Story story : stories) {
                story.setPost(post);
            }
        }

        // 게시물 저장
        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }

    /**
     * 게시물 엔티티를 DTO로 변환합니다.
     *
     * @param post 게시물 엔티티
     * @return 게시물 DTO
     */
    private PostDto convertToDto(Post post) {
        // 스토리 ID 리스트 생성
        List<Long> storyIds = post.getStories() != null
                ? post.getStories().stream()
                .map(Story::getId)
                .collect(Collectors.toList())
                : List.of();

        // PostDto 객체 생성 및 반환
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getStartDate(),
                post.getEndDate(),
                post.getLocation(),
                post.getCompanion(),
                storyIds
        );
    }

    /**
     * 특정 사용자가 작성한 게시물 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자가 작성한 게시물의 DTO 목록
     */
    @Transactional(readOnly = true)
    public List<PostDto> getPostsByUserId(Long userId) {
        // 사용자의 게시물 조회
        List<Post> posts = postRepository.findByUserId(userId);
        // 게시물 목록을 DTO로 변환하여 반환
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
