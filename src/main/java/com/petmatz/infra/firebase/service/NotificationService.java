package com.petmatz.infra.firebase.service;

import com.petmatz.infra.firebase.dto.FcmMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final StringRedisTemplate redisTemplate;
    private final PushNotificationService pushNotificationService;
    private final FcmTokenService fcmTokenService;

    private static final String NOTIFICATION_PREFIX = "offlineNotifications:";
    private static final String STATUS_PREFIX = "userStatus:";
    private static final long NOTIFICATION_TTL = 30; // 오프라인 알림 TTL (30일)

    // 오프라인 알림 저장
    public void saveOfflineNotification(String userId, String messageContent) {
        String key = NOTIFICATION_PREFIX + userId;
        redisTemplate.opsForList().rightPush(key, messageContent); // 메시지 저장
    }

    // 오프라인 알림 전송
    public void sendOfflineNotifications(String userId) {
        String key = NOTIFICATION_PREFIX + userId;
        List<String> notifications = redisTemplate.opsForList().range(key, 0, -1);
        redisTemplate.delete(key); // 전송 후 삭제

        if (notifications != null) {
            for (String messageContent : notifications) {
                // FCM 토큰 조회
                String fcmToken = fcmTokenService.getToken(userId);

                if (fcmToken != null) {
                    // FcmMessage 객체 생성
                    FcmMessage fcmMessage = FcmMessage.of(fcmToken, "오프라인 메시지", messageContent);

                    // FCM 알림 전송
                    pushNotificationService.sendChatNotification(fcmMessage);
                } else {
                    System.err.println("FCM 토큰을 찾을 수 없습니다: " + userId);
                }
            }
        }
    }



    // 4. FCM을 통한 채팅 알림 전송
    public void sendChatNotification(String userId, String title, String body) {
        String fcmToken = fcmTokenService.getToken(userId); // FcmTokenService를 통해 FCM 토큰 조회

        if (fcmToken != null) {
            pushNotificationService.sendChatNotification(FcmMessage.of(fcmToken, title, body)); // FCM 메시지 전송
        } else {
            System.err.println("FCM 토큰이 없습니다: " + userId); // 로그로 알림
        }
    }

    // 알림 처리
    public void handleNotification(String receiverId, String messageContent) {
        String key = STATUS_PREFIX + receiverId;
        boolean isOnline = "online".equals(redisTemplate.opsForValue().get(key));

        if (isOnline) {
            // FCM 메시지 객체 생성
            String fcmToken = fcmTokenService.getToken(receiverId); // FCM 토큰 조회
            if (fcmToken != null) {
                FcmMessage fcmMessage = FcmMessage.of(fcmToken, "새 메시지", messageContent);
                pushNotificationService.sendChatNotification(fcmMessage); // FCM 메시지 전송
            } else {
                System.err.println("FCM 토큰을 찾을 수 없습니다: " + receiverId);
            }
        } else {
            // 사용자 오프라인 상태일 경우 알림 저장
            saveOfflineNotification(receiverId, messageContent);
        }
    }

}




