package com.petmatz.api.chatting.dto;

import java.time.LocalDateTime;

public record ChatReadRequest(

        String chatRoomId,
        String userId,
        LocalDateTime lastReadMessageId

) {
}
