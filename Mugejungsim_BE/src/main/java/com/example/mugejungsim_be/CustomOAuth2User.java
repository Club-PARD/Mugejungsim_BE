package com.example.mugejungsim_be;

import com.example.mugejungsim_be.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Schema(description = "구글 OAuth2 로그인 사용자 정보")
public class CustomOAuth2User implements OAuth2User {

    @Schema(description = "사용자 속성 데이터", required = true)
    private final Map<String, Object> attributes;

    @Schema(description = "이름 속성 키", required = true)
    private final String nameAttributeKey;

    @Schema(description = "데이터베이스 사용자 엔티티")
    private final User user;

    public CustomOAuth2User(Map<String, Object> attributes, String nameAttributeKey, User user) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return (String) attributes.get(nameAttributeKey);
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getPicture() {
        return (String) attributes.get("picture"); // Google 프로필 이미지
    }

    public User getUser() {
        return user;
    }
}
