//package com.petmatz.api.user.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//public class KakaoController {
//
//    private final KakaoOAuthService kakaoOAuthService;
//
//    @GetMapping("/oauth2/callback/kakao")
//    public String kakaoLogin(@RequestParam String code) {
//        kakaoOAuthService.getKakaoUserInfo(code);
//        return "Login successful!";
//    }
//}