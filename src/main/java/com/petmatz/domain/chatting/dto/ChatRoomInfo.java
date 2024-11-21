package com.petmatz.domain.chatting.dto;

import lombok.Builder;

@Builder
public record ChatRoomInfo(

        String caregiverInfo,
        String entrustedInfo

) {
}
