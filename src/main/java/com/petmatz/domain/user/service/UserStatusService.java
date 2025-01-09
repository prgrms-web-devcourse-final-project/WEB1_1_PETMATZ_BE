package com.petmatz.domain.user.service;

import com.petmatz.infra.redis.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final StringRedisTemplate redisTemplate;
    private final RedisPublisher redisPublisher;
    private static final String STATUS_PREFIX = "userStatus:";
    private static final String ONLINE_TOPIC = "user-online";

    // 사용자 상태 업데이트 및 Pub/Sub 발행
    public void updateUserStatus(String userId, boolean isOnline) {
        String key = STATUS_PREFIX + userId;
        if (isOnline) {
            redisTemplate.opsForValue().set(key, "online", 30, TimeUnit.MINUTES); // TTL 설정
            redisPublisher.publish(ONLINE_TOPIC, userId); // Pub/Sub 발행
        } else {
            redisTemplate.delete(key);
        }
    }

    // 사용자 온라인 상태 확인
    public boolean isUserOnline(String userId) {
        String key = STATUS_PREFIX + userId;
        String status = redisTemplate.opsForValue().get(key);
        return "online".equals(status);
    }
}


