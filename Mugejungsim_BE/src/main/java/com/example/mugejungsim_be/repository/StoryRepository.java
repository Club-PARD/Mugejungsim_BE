package com.example.mugejungsim_be.repository;

import com.example.mugejungsim_be.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByUserId(Long userId);
    List<Story> findByPostId(Long postId);

}
