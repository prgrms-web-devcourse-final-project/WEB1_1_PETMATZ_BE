package com.petmatz.api.chatting;

import com.petmatz.api.chatting.dto.*;
import com.petmatz.api.global.dto.Response;

import com.petmatz.domain.chatting.dto.ChatMessageInfo;
import com.petmatz.domain.user.info.UserInfo;
import com.petmatz.domain.user.service.UserServiceImpl;

import com.petmatz.domain.chatting.ChatMessageService;
import com.petmatz.domain.chatting.ChatRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @MessageMapping("/chat")
    public void sendPrivateMessage(ChatMessageRequest chatMessageRequest) {
        ChatMessageInfo chatMessageInfo = chatMessageRequest.of();
        chatService.updateMessage(chatMessageInfo, chatMessageRequest.chatRoomId());
        String destination = "/topic/chat/" + chatMessageRequest.chatRoomId();
        simpMessagingTemplate.convertAndSend(destination, chatMessageInfo);
    }

    @MessageMapping("/chat/{chatRoomId}/read")
    public void sendReadStatus(@Payload ChatReadStatusDirect chatReadStatusDirect,
                               @DestinationVariable String chatRoomId) {
        String destination = "/topic/chat/" + chatRoomId;
        simpMessagingTemplate.convertAndSend(destination, chatReadStatusDirect);
    }


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

        return Response.success(ChatMessageResponse.of(
                chatMessageInfos.getContent()
                        .stream().map(ChatMessage::of).toList(),
                IChatUserResponse.of(userInfo),
                chatRoomId,
                chatMessageInfos.getNumber() + 1,
                chatMessageInfos.getTotalPages(),
                chatMessageInfos.getTotalElements()));
    }
}
