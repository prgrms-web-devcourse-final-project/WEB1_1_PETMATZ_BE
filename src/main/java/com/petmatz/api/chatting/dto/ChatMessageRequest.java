package com.petmatz.api.chatting.dto;

import com.petmatz.domain.chatting.dto.ChatMessageInfo;

import java.time.LocalDateTime;

public record ChatMessageRequest(


        String chatRoomId,

        String senderId,

        String receiverId,

        String msg


) {

    public ChatMessageInfo of() {
        return ChatMessageInfo.builder()
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .receiverId(receiverId)
                .msg(msg)
                .msgTimestamp(LocalDateTime.now())
                .build();
    }

}
