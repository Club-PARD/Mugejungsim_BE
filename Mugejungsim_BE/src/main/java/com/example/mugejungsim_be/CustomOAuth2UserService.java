package com.example.mugejungsim_be;

import com.example.mugejungsim_be.CustomOAuth2User;
import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);

            // OAuth2 제공자 정보 (google, kakao 등)
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String name = null;

            // 디버깅 로그: OAuth2 제공자 및 사용자 속성 출력
            System.out.println("Registration ID: " + registrationId);
            System.out.println("OAuth2User Attributes: " + oAuth2User.getAttributes());

            if ("google".equalsIgnoreCase(registrationId)) {
                // Google 사용자 정보 추출
                name = oAuth2User.getAttribute("name");
            } else if ("kakao".equalsIgnoreCase(registrationId)) {
                // Kakao 사용자 정보 추출
                Map<String, Object> attributes = oAuth2User.getAttributes();
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                if (kakaoAccount != null) {
                    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                    if (profile != null) {
                        name = (String) profile.get("nickname");
                    }
                }
            }

            // 사용자 이름이 없을 경우 기본값 설정
            String finalName = (name != null && !name.isEmpty()) ? name : "Anonymous User";

            // 사용자 검색 또는 생성
            User user = userRepository.findByNameAndProvider(finalName, registrationId)
                    .orElseGet(() -> {
                        // 새 사용자 저장
                        User newUser = new User(finalName, registrationId);
                        return userRepository.save(newUser);
                    });

            // 디버깅 로그: 사용자 정보 출력
            System.out.println("Processed User: " + user);

            return new CustomOAuth2User(oAuth2User.getAttributes(), user);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
