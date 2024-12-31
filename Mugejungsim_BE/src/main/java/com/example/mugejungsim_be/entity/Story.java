package com.example.mugejungsim_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Story ID

    @Column
    private String content; // Story content

    @Column(nullable = false)
    private String pid; // Unique Story Identifier

    @ElementCollection
    @CollectionTable(name = "story_categories", joinColumns = @JoinColumn(name = "story_id"))
    @Column(name = "category")
    private List<String> categories; // Multiple categories for the story

    private String imagePath; // Path to uploaded image (optional)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // Associated Post
}