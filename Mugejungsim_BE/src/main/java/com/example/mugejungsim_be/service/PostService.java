package com.example.mugejungsim_be.service;

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
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Transactional
    public Post createPost(User user, String title, String description, List<Long> storyIds) {
        if (storyIds == null || storyIds.isEmpty()) {
            throw new RuntimeException("Story IDs cannot be empty.");
        }

        List<Story> stories = storyRepository.findAllById(storyIds);
        if (stories.size() != storyIds.size()) {
            throw new RuntimeException("Some Story IDs are invalid.");
        }

        Post post = new Post();
        post.setUser(user);
        post.setTitle(title);
        post.setDescription(description);

        stories.forEach(post::addStory);
        return postRepository.save(post);
    }


    // Update an existing Post
    public Post updatePost(Long id, String title, String description, List<Long> storyIds, User user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found."));

        // Verify the user owns the post
        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User is not authorized to update this post.");
        }

        // Update the post's title and description
        post.setTitle(title);
        post.setDescription(description);

        // Update the stories associated with the post
        post.clearStories(); // Remove old stories
        List<Story> stories = storyRepository.findAllById(storyIds);
        if (stories.isEmpty()) {
            throw new RuntimeException("No stories found for the provided story IDs.");
        }

        for (Story story : stories) {
            post.addStory(story);
        }

        return postRepository.save(post);
    }

    // Delete a Post
    public void deletePost(Long postId, User user) {
        // Fetch the Post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Ensure the user owns the post
        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You do not have permission to delete this post.");
        }

        // Delete the Post
        postRepository.delete(post);
    }

    public List<Post> getPostsByUser(User user) {
        return postRepository.findByUser(user);
    }
}
