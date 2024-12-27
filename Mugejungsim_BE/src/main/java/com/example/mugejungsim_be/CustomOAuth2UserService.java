package com.example.mugejungsim_be;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * OAuth2 인증 과정을 처리하는 사용자 서비스
 *
 * 구글 OAuth2 로그인 시 사용자 정보를 로드하거나, 새로운 사용자를 데이터베이스에 저장합니다.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    /**
     * CustomOAuth2UserService 생성자
     *
     * @param userRepository 사용자 데이터베이스 저장소
     */
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * OAuth2 인증을 통해 사용자 정보를 로드하거나 새로운 사용자를 저장합니다.
     *
     * @param userRequest OAuth2 사용자 요청 객체
     * @return CustomOAuth2User 객체
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 부모 클래스의 사용자 정보 로드
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth2 제공자로부터 이메일과 이름 속성 추출
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // 사용자 정보가 데이터베이스에 존재하는지 확인
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    // 새로운 사용자 생성 및 저장
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    return userRepository.save(newUser);
                });

        // 사용자 정보를 CustomOAuth2User 객체로 래핑하여 반환
        return new CustomOAuth2User(oAuth2User.getAttributes(), "name", user);
    }
}
