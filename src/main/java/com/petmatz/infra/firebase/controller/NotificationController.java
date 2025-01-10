package com.petmatz.infra.firebase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.infra.firebase.dto.FcmMessage;
import com.petmatz.infra.redis.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final RedisPublisher redisPublisher;
    private final ObjectMapper objectMapper;

    @PostMapping
    public String sendNotification(@RequestBody FcmMessage fcmMessage){
        try {

            // 객체를 JSON 문자열로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String messageJson = objectMapper.writeValueAsString(fcmMessage);

            // Redis Pub/Sub 채널에 발행
            redisPublisher.publish("notifications", messageJson);

            return "메시지가 발행되었습니다.";
        } catch (JsonProcessingException e) {
            throw new RuntimeException("메시지 직렬화 실패: " + e.getMessage(), e);
        }
    }
}
