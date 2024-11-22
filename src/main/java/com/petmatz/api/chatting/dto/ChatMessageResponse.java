package com.petmatz.api.chatting.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        String senderId,

        String receiverId,

        String msg,

        Boolean readStatus,

        LocalDateTime msgTimestamp
) {


}
