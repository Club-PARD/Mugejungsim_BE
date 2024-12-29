package com.example.mugejungsim_be;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        try {
            // Load the default OAuth2 user
            OAuth2User oAuth2User = super.loadUser(userRequest);

            // Identify the provider (e.g., google, kakao)
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            System.out.println("Registration ID: " + registrationId);

            // Extract user information based on the provider
            String name = extractUserName(oAuth2User.getAttributes(), registrationId);

            // Use a default name if none is provided
            String finalName = (name != null && !name.isEmpty()) ? name : "Anonymous User";

            // Retrieve or create the user in the database
            User user = findOrCreateUser(finalName, registrationId);

            // Log the processed user information
            System.out.println("Processed User: " + user);

            // Return a custom OAuth2 user
            return new CustomOAuth2User(oAuth2User.getAttributes(), user);

        } catch (Exception e) {
            System.err.println("Error loading OAuth2 user: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Extracts the user name based on the OAuth2 provider.
     *
     * @param attributes      The attributes received from the provider.
     * @param registrationId  The provider ID (e.g., google, kakao).
     * @return The extracted user name.
     */
    private String extractUserName(Map<String, Object> attributes, String registrationId) {
        if ("google".equalsIgnoreCase(registrationId)) {
            // Extract name from Google attributes
            return (String) attributes.get("name");
        } else if ("kakao".equalsIgnoreCase(registrationId)) {
            // Extract name from Kakao attributes
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount != null) {
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile != null) {
                    return (String) profile.get("nickname");
                }
            }
        }
        return null; // Return null if no name is found
    }

    /**
     * Finds or creates a user in the database.
     *
     * @param name            The user name.
     * @param registrationId  The provider ID (e.g., google, kakao).
     * @return The user entity.
     */
    private User findOrCreateUser(String name, String registrationId) {
        Optional<User> existingUser = userRepository.findByNameAndProvider(name, registrationId);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        // Create a new user if not found
        User newUser = new User(name, registrationId);
        return userRepository.save(newUser);
    }
}
