package com.petmatz.api.chatting.dto;

import com.petmatz.domain.chatting.dto.ChatMessage;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder

public record ChatMessageResponse(
        String senderId,

        String receiverId,

        String msg,

        Boolean readStatus,

        LocalDateTime msgTimestamp
) {

    public static ChatMessageResponse of(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .senderId(chatMessage.getSenderId())
                .receiverId(chatMessage.getReceiverId())
                .msg(chatMessage.getMsg())
                .msgTimestamp(chatMessage.getMsgTimestamp())
//                .readStatus()
                .build();
    }

}
