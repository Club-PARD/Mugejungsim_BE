package com.example.mugejungsim_be.service;

import com.example.mugejungsim_be.entity.Story;
import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Transactional
    public Story createStory(User user, String content, String emoji) {
        if (content == null || emoji == null) {
            throw new IllegalArgumentException("Content or emoji cannot be null.");
        }

        Story story = new Story();
        story.setUser(user);
        story.setContent(content);
        story.setEmoji(emoji);

        return storyRepository.save(story);
    }

    public List<Story> getStoriesByUserId(Long userId) {
        return storyRepository.findByUserId(userId);
    }
}
