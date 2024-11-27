package com.petmatz.api.chatting.dto;

import com.petmatz.domain.chatting.dto.IChatUserInfo;
import lombok.Builder;

@Builder
public record IChatUserResponse(

        String userName,
        String profileURL

) {

    public static IChatUserResponse of(IChatUserInfo iChatUserInfo) {
        return IChatUserResponse.builder()
                .userName(iChatUserInfo.userEmail())
                .profileURL(iChatUserInfo.profileURL())
                .build();
    }

}
