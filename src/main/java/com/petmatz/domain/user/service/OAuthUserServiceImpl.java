package com.petmatz.domain.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.domain.user.entity.CustomOAuthUser;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);
        String oauthClientName = request.getClientRegistration().getClientName();
        System.out.println(oauthClientName);

        try {
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        User user = null;
        String accountId = null;
        String password = "Pa33w0rD";
        String email = "email@email.com";

        if(oauthClientName.equals("kakao")) {
            accountId = "kakao_" + oAuth2User.getAttributes().get("id");
            user = User.builder()
                    .accountId(accountId)
                    .password(password) // Make sure to hash the password before saving
                    .nickname("nickname")
                    .loginRole(User.LoginRole.ROLE_USER)
                    .gender(User.Gender.Male) // Convert string to Enum
                    .preferredSize(User.PreferredSize.Medium) // Convert string to Enum
                    .introduction("null")
                    .isCareAvailable(true)
                    .role(User.Role.Dol) // Assign a default role or map appropriately as needed
                    .loginType(User.LoginType.Kakao) // Default value or based on logic
                    .isRegistered(false) // You can set default values or change based on your logic
                    .recommendationCount(0) // Default value
                    .careCompletionCount(0) // Default value
                    .build();
            userRepository.save(user);
        }

        userRepository.save(user);

        return new CustomOAuthUser(accountId);
    }
}
