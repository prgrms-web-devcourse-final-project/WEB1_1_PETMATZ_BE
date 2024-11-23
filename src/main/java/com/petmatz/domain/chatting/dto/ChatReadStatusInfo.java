package com.petmatz.domain.chatting.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatReadStatusInfo(

        String chatRoomId,
        String userId,
        LocalDateTime lastReadMessageData


) {
}
