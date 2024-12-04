//package com.petmatz.api.user.controller;
//
//import com.petmatz.common.config.WebSecurityConfig;
//import com.petmatz.domain.user.constant.LoginType;
//import com.petmatz.domain.user.entity.User;
//import com.petmatz.domain.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Map;
//
//@RequiredArgsConstructor
//@Service
//public class KakaoOAuthService {
//
//    private final UserRepository userRepository;
//    private final WebClient webClient;
//    @Lazy // 순환 참조 해결
//    private final WebSecurityConfig webSecurityConfig;
//
//    @Value("${kakao.client-id}")
//    private String clientId;
//
//    @Value("${kakao.redirect-uri}")
//    private String redirectUri;
//
//    @Value("${kakao.client-secret}")
//    private String clientSecret;
//
//    @Value("${kakao.api.url}")
//    private String kakaoApiUrl;
//
//    public User getKakaoUserInfo(String code) {
//        // 1. Access Token 가져오기
//        String accessToken = getAccessToken(code);
//
//        // 2. 사용자 정보 요청
//        Map<String, Object> userInfo = getUserInfoFromKakao(accessToken);
//
//        // 3. 사용자 저장 또는 조회
//        return saveOrUpdateUser(userInfo);
//    }
//
//    private String getAccessToken(String code) {
//        ResponseEntity<Map> response = webClient.post()
//                .uri(uriBuilder -> uriBuilder
//                        .path(kakaoApiUrl + "/oauth/token")
//                        .queryParam("grant_type", "authorization_code")
//                        .queryParam("client_id", clientId)
//                        .queryParam("redirect_uri", redirectUri)
//                        .queryParam("code", code)
//                        .queryParam("client_secret", clientSecret)
//                        .build())
//                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
//                .retrieve()
//                .toEntity(Map.class)
//                .block();
//
//        return (String) response.getBody().get("access_token");
//    }
//
//    private Map<String, Object> getUserInfoFromKakao(String accessToken) {
//        return webClient.get()
//                .uri(kakaoApiUrl + "/v2/user/me")
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .retrieve()
//                .bodyToMono(Map.class)
//                .block();
//    }
//
//    private User saveOrUpdateUser(Map<String, Object> userInfo) {
//        String kakaoId = userInfo.get("id").toString();
//        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
//        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
//
//        String nickname = (String) profile.getOrDefault("nickname", "Unknown User");
//        String profileImg = (String) profile.getOrDefault("profile_image_url", "");
//
//        // 사용자 조회
//        User existingUser = userRepository.findByAccountId(kakaoId);
//
//        // 신규 사용자 생성 및 저장
//        User newUser = User.builder()
//                .accountId(kakaoId)
//                .nickname(nickname)
//                .profileImg(profileImg)
//                .loginType(LoginType.KAKAO)
//                .isRegistered(true)
//                .build();
//
//        return userRepository.save(newUser);
//
//    }
//}