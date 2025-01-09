package com.petmatz.infra.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(String topic, String message) {

        redisTemplate.convertAndSend(topic, message);
        redisTemplate.opsForList().rightPush("notifications", message); // Redis에 데이터 저장
    }
}
