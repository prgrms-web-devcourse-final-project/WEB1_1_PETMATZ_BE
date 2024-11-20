package com.petmatz.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.user.entity.CustomOAuthUser;
import com.petmatz.user.entity.UserEntity;
import com.petmatz.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

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

        UserEntity userEntity = null;
        String accountId = null;
        String password = "Pa33w0rD";
        String email = "email@email.com";

        if(oauthClientName.equals("kakao")) {
            accountId = "kakao_" + oAuth2User.getAttributes().get("id");
            userEntity = UserEntity.builder()
                    .accountId(accountId)
                    .password(password) // Make sure to hash the password before saving
                    .nickname("nickname")
                    .loginRole(UserEntity.LoginRole.ROLE_USER)
                    .gender(UserEntity.Gender.Male) // Convert string to Enum
                    .preferredSize(UserEntity.PreferredSize.Medium) // Convert string to Enum
                    .introduction("null")
                    .isCareAvailable(true)
                    .role(UserEntity.Role.Dol) // Assign a default role or map appropriately as needed
                    .loginType(UserEntity.LoginType.Kakao) // Default value or based on logic
                    .isRegistered(false) // You can set default values or change based on your logic
                    .recommendationCount(0) // Default value
                    .careCompletionCount(0) // Default value
                    .build();
            userRepository.save(userEntity);
        }

        userRepository.save(userEntity);

        return new CustomOAuthUser(accountId);
    }
}
