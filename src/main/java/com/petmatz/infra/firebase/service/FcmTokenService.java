package com.petmatz.infra.firebase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final StringRedisTemplate redisTemplate;
    private static final String TOKEN_PREFIX = "fcmToken:";

    // FCM 토큰 저장
    public void saveToken(String userId, String fcmToken) {
        String key = TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, fcmToken, 30, TimeUnit.DAYS); // 30일 TTL
    }

    // FCM 토큰 조회
    public String getToken(String userId) {
        String key = TOKEN_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }
}


