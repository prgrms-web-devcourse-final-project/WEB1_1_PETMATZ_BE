package com.petmatz.api.chatting;

import com.petmatz.api.chatting.dto.*;
import com.petmatz.api.global.dto.Response;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.chatting.ChatMessageService;
import com.petmatz.domain.chatting.ChatRoomService;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;
import com.petmatz.domain.user.info.UserInfo;
import com.petmatz.domain.user.service.UserNicknameService;
import com.petmatz.domain.user.service.UserServiceImpl;
import com.petmatz.domain.user.service.UserStatusService;
import com.petmatz.infra.firebase.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class ChatController {

    private final ChatMessageService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserServiceImpl userService;
    private final ChatRoomService chatRoomService;
    private final NotificationService notificationService;
    private final UserStatusService userStatusService;
    private final JwtExtractProvider jwtExtractProvider;
    private final UserNicknameService userNicknameService;


    //TODO 메세지 전송 ( 구독한 쪽으로 )
    @MessageMapping("/chat")
    public void sendPrivateMessage(ChatMessageRequest chatMessageRequest) {
        ChatMessageInfo chatMessageInfo = chatMessageRequest.of();
        chatService.updateMessage(chatMessageInfo, chatMessageRequest.chatRoomId());
        String destination = "/topic/chat/" + chatMessageRequest.chatRoomId();
        simpMessagingTemplate.convertAndSend(destination, chatMessageInfo);

        // 알림 처리
        String senderId = jwtExtractProvider.findAccountIdFromJwt();
        String receiverId = chatMessageRequest.receiverEmail();
        String messageContent = chatMessageRequest.msg();

        boolean isReceiverOnline = userStatusService.isUserOnline(receiverId);

        if (isReceiverOnline) {
            // 수신자가 온라인 상태일 때 FCM 푸시 알림 전송
            notificationService.sendChatNotification(receiverId, "새 메시지", messageContent);
        } else {
            // 수신자가 오프라인 상태일 때 Redis에 메시지 저장
            notificationService.saveOfflineNotification(receiverId, messageContent);
        }
    }

    // TODO 멍멍이 부탁등록 실시간 API 하나 파기
    // TODO 멍멍이 부탁같은 경우 채팅 DTO랑 똑같이 만들기

    //TODO 메세지 읽음 처리 ( 구독한 쪽으로 ), senderId, chatRoomId
    //TDO 상대편 마지막 읽음 시간 재측정,
    @MessageMapping("/chat/{chatRoomId}/read")
    public void sendReadStatus(@Payload ChatReadStatusDirect chatReadStatusDirect,
                               @DestinationVariable String chatRoomId) {
        String destination = "/topic/chat/" + chatRoomId;
        simpMessagingTemplate.convertAndSend(destination, chatReadStatusDirect);
    }


    //TODO sender는 Token 파싱해서 사용, Pagin적용해서 보내기
    //TODO 토큰으로 해당 채팅방에 사용자가 있는지 판단
    //TDO readStatus, enderId, receverId
    @GetMapping("/chat/message")
    @Operation(summary = "메세지 내역 긁어오기", description = "채팅방의 메세지 내역을 긁어오는 API")
    @Parameters({
            @Parameter(name = "chatRoomId", description = "채팅방 번호", example = "1"),
            @Parameter(name = "pageSize", description = "긁어올 페이지의 사이즈", example = "20 ( Default : 15 )"),
            @Parameter(name = "startPage", description = "현재 페이지의 번호 ( 0은 안됨!! )", example = "3 ( Default 1 )"),
            @Parameter(name = "lastReadTimestamp", description = "현재 페이지의 번호 ( 0은 안됨!! )", example = "3 ( Default 1 )")
    })
    public Response<ChatMessageResponse> selectChatMessage(
                                         @RequestParam String chatRoomId,
                                         @RequestParam(required = false) @Nullable LocalDateTime lastFetchTimestamp,
                                         @RequestParam(defaultValue = "10") int pageSize,
                                         @RequestParam(defaultValue = "1") int startPage
    ) {

        String receiverEmail = chatRoomService.selectChatRoomUserInfo(chatRoomId);
        Page<ChatMessageInfo> chatMessageInfos = chatService.selectMessage(receiverEmail, chatRoomId, startPage, pageSize, lastFetchTimestamp);
        UserInfo userInfo = userService.selectUserInfo(receiverEmail);


        log.info("chatMessageInfos.getContent() : " + chatMessageInfos.getContent());
        return Response.success(ChatMessageResponse.of(
                chatMessageInfos.getContent()
                        .stream().map(ChatMessage::of).toList(),
                IChatUserResponse.of(userInfo),
                chatRoomId,
                chatMessageInfos.getNumber() + 1,
                chatMessageInfos.getTotalPages(),
                chatMessageInfos.getTotalElements()));
    }

    //푸시알림 테스트용 api
    @PostMapping("/chat")
    public ResponseEntity<String> sendChatMessage(@RequestBody ChatMessageRequest chatMessageRequest) {
        ChatMessageInfo chatMessageInfo = chatMessageRequest.of();
        chatService.updateMessage(chatMessageInfo, chatMessageRequest.chatRoomId());

        // 메시지 브로드캐스트
        String destination = "/topic/chat/" + chatMessageRequest.chatRoomId();
        simpMessagingTemplate.convertAndSend(destination, chatMessageInfo);

        // 알림 처리
        String senderId = jwtExtractProvider.findAccountIdFromJwt();
        String receiverId = chatMessageRequest.receiverEmail();
        String messageContent = chatMessageRequest.msg();

        boolean isReceiverOnline = userStatusService.isUserOnline(receiverId);

        if (isReceiverOnline) {
            // 수신자가 온라인 상태일 때 FCM 푸시 알림 전송
            notificationService.sendChatNotification(receiverId, "새 메시지", messageContent);
        } else {
            // 수신자가 오프라인 상태일 때 Redis에 메시지 저장
            notificationService.saveOfflineNotification(receiverId, messageContent);
        }

        return ResponseEntity.ok("메시지가 성공적으로 전송되었습니다.");
    }

}
