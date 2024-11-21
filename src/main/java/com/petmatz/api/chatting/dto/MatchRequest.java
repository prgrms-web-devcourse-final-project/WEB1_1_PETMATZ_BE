package com.petmatz.api.chatting.dto;

import com.petmatz.domain.chatting.dto.ChatRoomInfo;

public record MatchRequest(

        String caregiverInfo,
        String entrustedInfo

) {

    public ChatRoomInfo of() {
        return ChatRoomInfo.builder()
                .caregiverInfo(caregiverInfo)
                .entrustedInfo(entrustedInfo)
                .build();
    }
}
