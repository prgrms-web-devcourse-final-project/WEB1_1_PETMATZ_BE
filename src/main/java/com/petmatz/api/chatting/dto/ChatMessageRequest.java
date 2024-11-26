package com.petmatz.api.chatting.dto;

import com.petmatz.domain.chatting.dto.ChatMessageInfo;

import java.time.LocalDateTime;

public record ChatMessageRequest(


        String chatRoomId,

        String senderEmail,

        String receiverEmail,

        String msg,

        String msg_type


) {

    public ChatMessageInfo of() {
        return ChatMessageInfo.builder()
                .senderEmail(senderEmail)
                .receiverEmail(receiverEmail)
                .msg(msg)
                .msgTimestamp(LocalDateTime.now())
                .build();
    }

}
