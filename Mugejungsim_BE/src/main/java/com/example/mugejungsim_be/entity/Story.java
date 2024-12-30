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

    @Column(nullable = false)
    private String content; // Story content

    @Column(nullable = false)
    private String pid; // Story content

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Associated User

    @ElementCollection
    @CollectionTable(name = "story_categories", joinColumns = @JoinColumn(name = "story_id"))
    @Column(name = "category")
    private List<String> categories; // Multiple categories for the story

    private String imagePath; // Path to uploaded image (optional)

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer orderIndex; // Order index for sorting

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // Associated Post

}
