package com.petmatz.infra.firebase.controller;

import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.infra.firebase.service.FcmTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fcm")
public class FcmController {

    private final FcmTokenService fcmTokenService;
    private final JwtExtractProvider jwtExtractProvider;

    @PostMapping("/register")
    public ResponseEntity<String> registerFcmToken(@RequestBody Map<String, String> request) {
        String userId = jwtExtractProvider.findAccountIdFromJwt();
        String fcmToken = request.get("fcmToken");

        if (fcmToken != null) {
            fcmTokenService.saveToken(userId, fcmToken);
            return ResponseEntity.ok("FCM 토큰이 등록되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("FCM 토큰이 누락되었습니다.");
        }
    }
}

