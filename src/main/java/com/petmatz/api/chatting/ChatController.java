package com.petmatz.api.chatting;

import com.petmatz.api.chatting.dto.ChatMessageRequest;
import com.petmatz.api.global.dto.Response;
import com.petmatz.domain.chatting.ChatService;
import com.petmatz.domain.chatting.dto.ChatMessage;
import com.petmatz.domain.chatting.dto.ChatRoomDocs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat1")
    public void sendPrivateMessage(ChatMessageRequest chatMessageRequest) {
        System.out.println(chatMessageRequest.toString());
        chatService.saveChat(chatMessageRequest.of());
        System.out.println(chatMessageRequest.msg());
        simpMessagingTemplate.convertAndSendToUser(
                chatMessageRequest.receiverId(),
                "/queue/messages",
                chatMessageRequest
        );
    }

    @GetMapping("/chat/message")
    @Operation(summary = "메세지 내역 긁어오기", description = "채팅방의 메세지 내역을 긁어오는 API")
    @Parameters({
            @Parameter(name = "chatRoomId", description = "채팅방 번호", example = "1"),
            @Parameter(name = "pageSize", description = "긁어올 페이지의 사이즈", example = "20 ( Default : 15 )"),
            @Parameter(name = "startPage", description = "현재 페이지의 번호 ( 0은 안됨!! )", example = "3 ( Default 1 )")
    })
    public Response<?> selectChatMessage(@RequestParam String chatRoomId,
                                         @RequestParam(defaultValue = "15") int pageSize,
                                         @RequestParam(defaultValue = "1") int startPage
    ) {

        List<ChatMessage> chatRoomDocs = chatService.selectMessage(chatRoomId,startPage ,pageSize);
        return Response.success(chatRoomDocs);
    }
}
