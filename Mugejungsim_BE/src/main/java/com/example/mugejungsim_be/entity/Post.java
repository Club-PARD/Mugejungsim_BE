package com.example.mugejungsim_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Story> stories = new ArrayList<>();

    // Method to add a story to the post
    public void addStory(Story story) {
        if (!stories.contains(story)) {
            stories.add(story);
            story.setPost(this);
        }
    }

    // Method to remove a story from the post
    public void removeStory(Story story) {
        if (stories.contains(story)) {
            stories.remove(story);
            story.setPost(null);
        }
    }

    // Method to clear all stories from the post
    public void clearStories() {
        for (Story story : new ArrayList<>(stories)) {
            story.setPost(null); // Break the relationship on the Story side
        }
        stories.clear(); // Clear the stories list
    }
}
