package com.petmatz.domain.user.service;

import com.petmatz.domain.user.constant.LoginRole;
import com.petmatz.domain.user.constant.LoginType;
import com.petmatz.domain.user.entity.CustomOAuthUser;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuthUserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository; // 사용자 저장소

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 1. Check the registration ID (ensure it's Kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (!"kakao".equals(registrationId)) {
            throw new OAuth2AuthenticationException("Unsupported registrationId: " + registrationId);
        }

        // 2. Extract attributes
        String kakaoAccountId = attributes.get("id").toString(); // 고유 ID
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        // Extract email (required for accountId)
        String email = (String) kakaoAccount.get("email");
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email is required for Kakao login.");
        }

        // Extract nickname
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = (String) profile.getOrDefault("nickname", "Unknown User");

        // Extract profile image (optional)
        String profileImage = (String) profile.getOrDefault("profile_image_url", "");

        // 3. Check if user exists or create new user
        User user = userRepository.findByAccountId(email);
        if (user == null) {
            user = createNewKakaoUser(email, nickname, profileImage); // 신규 사용자 생성
        }

        // 4. Return CustomOAuthUser
        return new CustomOAuthUser(user.getId(), user.getAccountId(), attributes, oAuth2User.getAuthorities());
    }

    private User createNewKakaoUser(String email, String nickname, String profileImage) {
        User newUser = User.builder()
                .accountId(email) // 이메일을 accountId에 저장
                .password("default_password") // 기본 비밀번호 설정 (필요시 변경)
                .nickname(nickname)
                .profileImg(profileImage) // 프로필 이미지 저장
                .isCareAvailable(true)
                .mbti("ISTJ")
                .loginRole(LoginRole.ROLE_USER) // 기본 역할 설정
                .loginType(LoginType.KAKAO) // 로그인 타입
                .build();

        return userRepository.save(newUser);
    }
}