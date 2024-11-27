package com.petmatz.api.chatting.dto;

import com.petmatz.domain.chatting.dto.ChatReadStatusInfo;

import java.time.LocalDateTime;

public record ChatReadRequest(

        String chatRoomId,
        String userEmail,
        LocalDateTime lastReadMessageData

) {

    public ChatReadStatusInfo of() {
        return ChatReadStatusInfo.builder()
                .chatRoomId(chatRoomId)
                .userEmail(userEmail)
                .lastReadMessageData(lastReadMessageData)
                .build();
    }

}
