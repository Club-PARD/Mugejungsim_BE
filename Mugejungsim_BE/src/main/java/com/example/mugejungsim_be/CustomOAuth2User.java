package com.example.mugejungsim_be;

import com.example.mugejungsim_be.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 구글 OAuth2 로그인 사용자 정보를 나타내는 클래스
 *
 * OAuth2 인증 과정에서 사용자 정보를 관리하며, 데이터베이스 사용자 엔티티(User)와 연결됩니다.
 */
@Schema(description = "구글 OAuth2 로그인 사용자 정보")
public class CustomOAuth2User implements OAuth2User {

    @Schema(description = "OAuth2 제공자로부터 제공받은 사용자 속성 데이터", required = true, example = "{\"email\": \"example@gmail.com\", \"name\": \"John Doe\"}")
    private final Map<String, Object> attributes;

    @Schema(description = "OAuth2 제공자의 이름 속성 키", required = true, example = "name")
    private final String nameAttributeKey;

    @Schema(description = "애플리케이션 데이터베이스에 저장된 사용자 정보")
    private final User user;

    /**
     * CustomOAuth2User 생성자
     *
     * @param attributes       OAuth2 제공자로부터 제공받은 사용자 속성
     * @param nameAttributeKey 사용자 이름 속성 키
     * @param user             데이터베이스 사용자 엔티티
     */
    public CustomOAuth2User(Map<String, Object> attributes, String nameAttributeKey, User user) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.user = user;
    }

    /**
     * 사용자 속성 데이터를 반환합니다.
     *
     * @return 사용자 속성 데이터 맵
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * 사용자 권한을 반환합니다. (현재 비어 있음)
     *
     * @return 비어 있는 권한 컬렉션
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * 사용자 이름을 반환합니다.
     *
     * @return 사용자 이름
     */
    @Override
    public String getName() {
        return (String) attributes.get(nameAttributeKey);
    }

    /**
     * 사용자 이메일을 반환합니다.
     *
     * @return 사용자 이메일
     */
    @Schema(description = "사용자 이메일", example = "example@gmail.com")
    public String getEmail() {
        return (String) attributes.get("email");
    }

    /**
     * 사용자 프로필 이미지를 반환합니다.
     *
     * @return 사용자 프로필 이미지 URL
     */
    @Schema(description = "사용자 프로필 이미지 URL", example = "https://example.com/profile.jpg")
    public String getPicture() {
        return (String) attributes.get("picture");
    }

    /**
     * 데이터베이스 사용자 엔티티를 반환합니다.
     *
     * @return 데이터베이스 사용자 객체
     */
    @Schema(description = "데이터베이스 사용자 객체")
    public User getUser() {
        return user;
    }
}
