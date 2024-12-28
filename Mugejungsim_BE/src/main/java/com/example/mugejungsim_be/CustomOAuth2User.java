package com.example.mugejungsim_be;

import com.example.mugejungsim_be.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final Map<String, Object> attributes;
    private final User user;

    public CustomOAuth2User(Map<String, Object> attributes, User user) {
        this.attributes = attributes;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한이 필요하면 추가
    }

    @Override
    public String getName() {
        return user.getName(); // User 엔티티의 이름 반환
    }

    public String getProfileImage() {
        return user.getProfileImageUrl(); // User 엔티티의 프로필 이미지 반환
    }

    public Long getId() {
        return user.getId(); // User 엔티티의 고유 ID 반환
    }

    public User getUser() {
        return user;
    }
}
