package com.petmatz.domain.chatting.dto;

import lombok.Builder;

@Builder
public record ChatRoomListInfo(

        String chatRoomId,
        String chatRoomName

) {
}
