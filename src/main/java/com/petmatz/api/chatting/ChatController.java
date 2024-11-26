package com.petmatz.api.chatting;

import com.petmatz.api.chatting.dto.ChatMessage;
import com.petmatz.api.chatting.dto.ChatMessageRequest;
import com.petmatz.api.chatting.dto.ChatMessageResponse;
import com.petmatz.api.chatting.dto.ChatReadStatusDirect;
import com.petmatz.api.global.dto.Response;
import com.petmatz.domain.chatting.ChatMessageService;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class ChatController {

    private final ChatMessageService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    //TODO 메세지 전송 ( 구독한 쪽으로 )
    @MessageMapping("/chat")
    public void sendPrivateMessage(ChatMessageRequest chatMessageRequest) {
        ChatMessageInfo chatMessageInfo = chatMessageRequest.of();
        chatService.updateMessage(chatMessageInfo, chatMessageRequest.chatRoomId());
        String destination = "/topic/chat/" + chatMessageRequest.chatRoomId();
        simpMessagingTemplate.convertAndSend(destination, chatMessageInfo);
    }

    // TODO 멍멍이 부탁등록 실시간 API 하나 파기
    // TODO 멍멍이 부탁같은 경우 채팅 DTO랑 똑같이 만들기

    //TODO 메세지 읽음 처리 ( 구독한 쪽으로 ), senderId, chatRoomId
    @MessageMapping("/chat/{chatRoomId}/read")
    public void sendReadStatus(@Payload ChatReadStatusDirect chatReadStatusDirect,
                               @DestinationVariable String chatRoomId) {
        String destination = "/topic/chat/" + chatRoomId;
        simpMessagingTemplate.convertAndSend(destination, chatReadStatusDirect);
    }


    //TODO sender는 Token 파싱해서 사용, Pagin적용해서 보내기
    @GetMapping("/chat/message")
    @Operation(summary = "메세지 내역 긁어오기", description = "채팅방의 메세지 내역을 긁어오는 API")
    @Parameters({
            @Parameter(name = "sender", description = "메세지를 보낸 유저의 ID", example = "테스트"),
            @Parameter(name = "receiver", description = "메세지를 받는 유저의 ID", example = "2"),
            @Parameter(name = "chatRoomId", description = "채팅방 번호", example = "1"),
            @Parameter(name = "pageSize", description = "긁어올 페이지의 사이즈", example = "20 ( Default : 15 )"),
            @Parameter(name = "startPage", description = "현재 페이지의 번호 ( 0은 안됨!! )", example = "3 ( Default 1 )")
    })
    public Response<?> selectChatMessage(
                                         @RequestParam String senderEmail,
                                         @RequestParam String receiverEmail,
                                         @RequestParam String chatRoomId,
                                         @RequestParam(defaultValue = "15") int pageSize,
                                         @RequestParam(defaultValue = "1") int startPage
    ) {

        Page<ChatMessageInfo> chatMessageInfos = chatService.selectMessage(senderEmail, receiverEmail, chatRoomId, startPage, pageSize);
        //User 조회하는거 필요

        log.info("chatMessageInfos.getContent() : " + chatMessageInfos.getContent());
        return Response.success(ChatMessageResponse.of(
                chatMessageInfos.getContent()
                        .stream().map(ChatMessage::of).toList(),
                null,
                chatRoomId,
                chatMessageInfos.getNumber() + 1,
                chatMessageInfos.getTotalPages(),
                chatMessageInfos.getTotalElements()));
    }
}
