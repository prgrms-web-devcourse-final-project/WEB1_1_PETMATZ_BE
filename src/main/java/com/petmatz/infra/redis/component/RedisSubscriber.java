package com.petmatz.infra.redis.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.infra.firebase.dto.FcmMessage;
import com.petmatz.infra.firebase.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {

    private final PushNotificationService pushNotificationService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
        String topic = new String(pattern);
        String payload = new String(message.getBody());

        log.info("Received event from topic '{}'", topic);

        try {
            // 1차 역직렬화: 이스케이프된 JSON 문자열 처리
            String unescapedPayload = objectMapper.readValue(payload, String.class);

            // 2차 역직렬화: FcmMessage 객체로 변환
            FcmMessage fcmMessage = objectMapper.readValue(unescapedPayload, FcmMessage.class);

            // FCM 푸시 알림 전송
            pushNotificationService.sendChatNotification(fcmMessage);
        } catch (Exception e) {
            log.error("푸시 알림 전송 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}



