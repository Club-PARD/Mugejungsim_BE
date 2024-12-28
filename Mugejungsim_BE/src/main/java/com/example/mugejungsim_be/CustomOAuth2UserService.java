package com.example.mugejungsim_be;

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

            // 사용자 데이터 저장 또는 업데이트
            String finalName = (name != null && !name.isEmpty()) ? name : "Anonymous User";

            // 새로운 사용자 생성 또는 기존 사용자 반환
            User user = userRepository.save(new User(finalName, registrationId));

            // 디버깅 로그: 저장된 사용자 정보 출력
            System.out.println("Saved User: " + user);

            // 사용자 정보를 CustomOAuth2User로 반환
            return new CustomOAuth2User(oAuth2User.getAttributes(), user);
        } catch (Exception e) {
            // 예외 출력
            e.printStackTrace();
            throw e; // Spring Security가 처리하도록 예외 전달
        }
    }
}
