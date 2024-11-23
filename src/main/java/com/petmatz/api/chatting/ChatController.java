package com.petmatz.api.chatting;

import com.petmatz.api.chatting.dto.ChatMessageRequest;
import com.petmatz.api.chatting.dto.ChatMessageResponse;
import com.petmatz.api.chatting.dto.ChatReadRequest;
import com.petmatz.api.global.dto.Response;
import com.petmatz.domain.chatting.ChatService;
import com.petmatz.domain.chatting.dto.ChatMessage;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void sendPrivateMessage(ChatMessageRequest chatMessageRequest) {
        ChatMessageInfo chatMessageInfo = chatMessageRequest.of();
        chatService.updateMessage(chatMessageInfo);
        String destination = "/topic/chat/" + chatMessageRequest.chatRoomId();
        simpMessagingTemplate.convertAndSend(destination, chatMessageInfo);
    }

    // TODO 멍멍이 부탁등록 실시간 API 하나 파기
    // TODO 채팅방 목록 뿌릴 경우 다 연결하기, 메타정보 데이타 넘겨주기


    // TODO 읽음, 안읽음 만들기
    @MessageMapping("/read")
    public void markAsRead(ChatReadRequest chatReadRequest) {
        chatService.updateMessageStatusRead(chatReadRequest.of());
    }


    @GetMapping("/chat/message")
    @Operation(summary = "메세지 내역 긁어오기", description = "채팅방의 메세지 내역을 긁어오는 API")
    @Parameters({
            @Parameter(name = "userId", description = "유저의 ID", example = "테스트"),
            @Parameter(name = "chatRoomId", description = "채팅방 번호", example = "1"),
            @Parameter(name = "pageSize", description = "긁어올 페이지의 사이즈", example = "20 ( Default : 15 )"),
            @Parameter(name = "startPage", description = "현재 페이지의 번호 ( 0은 안됨!! )", example = "3 ( Default 1 )")
    })
    public Response<?> selectChatMessage(@RequestParam String userId,
                                         @RequestParam String chatRoomId,
                                         @RequestParam(defaultValue = "15") int pageSize,
                                         @RequestParam(defaultValue = "1") int startPage
    ) {

        List<ChatMessageResponse> chatMessages = chatService.selectMessage(userId, chatRoomId, startPage, pageSize).stream().map(
                ChatMessageResponse::of
        ).toList();

        return Response.success(chatMessages);
    }
}
