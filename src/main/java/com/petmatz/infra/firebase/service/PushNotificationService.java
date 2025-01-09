package com.petmatz.infra.firebase.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.petmatz.domain.user.service.UserNicknameService;
import com.petmatz.infra.firebase.dto.FcmMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationService {

    private final FcmTokenService fcmTokenService;
    private final UserNicknameService userNicknameService;

    public void sendChatNotification(FcmMessage fcmMessage) {
        String senderNickname = userNicknameService.getNicknameByEmail();

        if (fcmMessage.getToken() != null) {
            try {
                Message message = Message.builder()
                        .setToken(fcmMessage.getToken())
                        .setNotification(Notification.builder()
                                .setTitle(senderNickname)
                                .setBody(fcmMessage.getBody())
                                .build())
                        .build();

                String response = FirebaseMessaging.getInstance().send(message);
                log.info("푸시 알림 전송 성공: {}", response);
            } catch (FirebaseMessagingException e) {
                log.error("푸시 알림 전송 실패: {}", e.getMessage());
                log.error("오류 코드: {}", e.getMessagingErrorCode());
            } catch (Exception e) {
                log.error("알 수 없는 오류 발생: {}", e.getMessage());
            }
        } else {
            log.warn("FCM 토큰이 없습니다. 알림 전송 실패.");
        }
    }
}



