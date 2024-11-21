package com.petmatz.domain.chatting.dto;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessageInfo(

        String message1,

        String chatRoomId,

        String senderId,

        String receiverId,

        String msg,

        LocalDateTime msgTimestamp

) {

    public ChatMessage of() {
        return ChatMessage.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .msg(msg)
                .msgTimestamp(msgTimestamp)
                .build();
    }

}
