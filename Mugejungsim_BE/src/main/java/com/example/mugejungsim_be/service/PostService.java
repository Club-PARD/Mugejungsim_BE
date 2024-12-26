package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.dto.PostDto;
import com.example.mugejungsim_be.dto.StoryDto;
import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.Story;
import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.PostRepository;
import com.example.mugejungsim_be.repository.StoryRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Transactional
    public PostDto createPost(User user, PostDto postDto) {
        if (postDto.getStoryIds() == null || postDto.getStoryIds().isEmpty()) {
            throw new IllegalArgumentException("Post must include at least one story.");
        }

        // Fetch stories by IDs
        List<Story> stories = storyRepository.findAllById(postDto.getStoryIds());
        if (stories.isEmpty()) {
            throw new IllegalArgumentException("Invalid story IDs provided.");
        }

        // Ensure all stories belong to the user
        for (Story story : stories) {
            if (!story.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("One or more stories do not belong to the user.");
            }
        }

        // Create the Post entity
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setUser(user);
        post.setStories(stories);

        // Link stories to the post
        for (Story story : stories) {
            story.setPost(post);
        }

        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }

    private PostDto convertToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setStoryIds(post.getStories().stream()
                .map(Story::getId)
                .collect(Collectors.toList()));
        return postDto;
    }
    @Transactional(readOnly = true)
    public List<PostDto> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);

        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<StoryDto> getStoriesByPostId(Long postId) {
        List<Story> stories = storyRepository.findByPostId(postId);

        if (stories == null || stories.isEmpty()) {
            throw new RuntimeException("No stories found for post ID: " + postId);
        }

        return stories.stream()
                .map(StoryDto::new)
                .collect(Collectors.toList());
    }



}
