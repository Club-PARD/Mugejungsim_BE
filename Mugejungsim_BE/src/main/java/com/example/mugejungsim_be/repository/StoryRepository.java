package com.example.mugejungsim_be.repository;

import com.example.mugejungsim_be.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByPostId(Long postId);

    @Query("SELECT MAX(s.orderIndex) FROM Story s WHERE s.post.id = :postId")
    Integer findMaxOrderIndexByPostId(@Param("postId") Long postId);
}

