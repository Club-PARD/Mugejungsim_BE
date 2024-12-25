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
        Story story = new Story();
        story.setUser(user);
        story.setContent(content);
        story.setEmoji(emoji);

        System.out.println("Saving story: " + story);
        return storyRepository.save(story);
    }

    public List<Story> getStoriesByUserId(Long userId) {
        return storyRepository.findByUserId(userId);
    }

    public Story updateStory(Long id, User user, String content, String emoji) {
        Story story = storyRepository.findById(id).orElse(null);

        if (story == null || !story.getUser().getId().equals(user.getId())) {
            return null; // 스토리가 없거나 사용자 소유가 아닐 경우 null 반환
        }

        story.setContent(content);
        story.setEmoji(emoji);

        return storyRepository.save(story);
    }

    public boolean deleteStory(Long id, User user) {
        Story story = storyRepository.findById(id).orElse(null);

        if (story == null || !story.getUser().getId().equals(user.getId())) {
            return false; // 스토리가 없거나 사용자 소유가 아닐 경우 false 반환
        }

        storyRepository.delete(story);
        return true;
    }
}
