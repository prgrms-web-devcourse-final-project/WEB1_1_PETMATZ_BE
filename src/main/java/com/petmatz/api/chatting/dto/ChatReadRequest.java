package com.petmatz.api.chatting.dto;

import com.petmatz.domain.chatting.dto.ChatReadStatusInfo;

import java.time.LocalDateTime;

public record ChatReadRequest(

        String chatRoomId,
        String userId,
        LocalDateTime lastReadMessageData

) {

    public ChatReadStatusInfo of() {
        return ChatReadStatusInfo.builder()
                .chatRoomId(chatRoomId)
                .userId(userId)
                .lastReadMessageData(lastReadMessageData)
                .build();
    }

}
