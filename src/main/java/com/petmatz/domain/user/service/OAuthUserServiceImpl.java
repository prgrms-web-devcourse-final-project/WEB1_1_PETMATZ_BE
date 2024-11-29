package com.petmatz.domain.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.domain.user.constant.Gender;
import com.petmatz.domain.user.constant.LoginRole;
import com.petmatz.domain.user.constant.LoginType;
import com.petmatz.domain.user.constant.Role;
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

        try {
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 공통 변수 초기화
        User user;
        Long userId = null;
        String accountId;
        String defaultPassword = "Pa33w0rD"; // 기본 비밀번호 설정 (해싱 필수)
        String email = (String) oAuth2User.getAttributes().getOrDefault("email", "email@email.com");

        // OAuth 클라이언트별 처리
        if (oauthClientName.equalsIgnoreCase("kakao")) {
            accountId = "kakao_" + oAuth2User.getAttributes().get("id");

            // 사용자 존재 여부 확인
            user = userRepository.findByAccountId(accountId);
            if (user == null) {
                // 새로운 사용자 생성
                user = User.builder()
                        .accountId(accountId)
                        .password(defaultPassword) // 해싱 필수
                        .nickname("nickname") // 기본 닉네임 설정
                        .loginRole(LoginRole.ROLE_USER)
                        .gender(Gender.FEMALE) // 기본값
                        .preferredSizes(null) // 설정 필요
                        .introduction(null) // 기본 소개
                        .isCareAvailable(true)
                        .role(Role.DOL) // 기본 역할 설정
                        .loginType(LoginType.KAKAO) // 로그인 유형
                        .isRegistered(false) // 기본 등록 상태
                        .recommendationCount(0)
                        .careCompletionCount(0)
                        .build();
                user = userRepository.save(user);
            }
            userId = user.getId();
        } else {
            throw new OAuth2AuthenticationException("Unsupported OAuth provider: " + oauthClientName);
        }

        // CustomOAuthUser 생성 및 반환
        return new CustomOAuthUser(userId, user.getAccountId(), oAuth2User.getAttributes(), oAuth2User.getAuthorities());
    }
}