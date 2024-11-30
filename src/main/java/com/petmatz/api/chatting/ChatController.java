package com.petmatz.api.chatting;

import com.petmatz.api.chatting.dto.*;
import com.petmatz.api.global.dto.Response;
import com.petmatz.domain.chatting.ChatMessageService;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;
import com.petmatz.domain.user.info.UserInfo;
import com.petmatz.domain.user.service.UserServiceImpl;
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
    private final UserServiceImpl userService;


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
    //TODO 토큰으로 해당 채팅방에 사용자가 있는지 판단
    @GetMapping("/chat/message")
    @Operation(summary = "메세지 내역 긁어오기", description = "채팅방의 메세지 내역을 긁어오는 API")
    @Parameters({
            @Parameter(name = "receiver", description = "메세지를 받는 유저의 ID", example = "2"),
            @Parameter(name = "chatRoomId", description = "채팅방 번호", example = "1"),
            @Parameter(name = "pageSize", description = "긁어올 페이지의 사이즈", example = "20 ( Default : 15 )"),
            @Parameter(name = "startPage", description = "현재 페이지의 번호 ( 0은 안됨!! )", example = "3 ( Default 1 )")
    })
    public Response<ChatMessageResponse> selectChatMessage(
                                         @RequestParam String receiverEmail,
                                         @RequestParam String chatRoomId,
                                         @RequestParam(defaultValue = "15") int pageSize,
                                         @RequestParam(defaultValue = "1") int startPage
    ) {

        Page<ChatMessageInfo> chatMessageInfos = chatService.selectMessage(receiverEmail, chatRoomId, startPage, pageSize);
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
}
