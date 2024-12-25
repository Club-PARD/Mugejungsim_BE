package com.example.mugejungsim_be.repository;

import com.example.mugejungsim_be.entity.Post;
import com.example.mugejungsim_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
}
