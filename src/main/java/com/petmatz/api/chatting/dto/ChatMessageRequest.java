package com.petmatz.api.chatting.dto;

import com.petmatz.domain.chatting.dto.ChatMessageInfo;

import java.time.LocalDateTime;

public record ChatMessageRequest(

        String message1,

        String chatRoomId,

        String senderId,

        String receiverId,

        String msg


) {

    public ChatMessageInfo of() {
        return ChatMessageInfo.builder()
                .message1(message1)
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .receiverId(receiverId)
                .msg(msg)
                .msgTimestamp(LocalDateTime.now())
                .build();
    }

}
