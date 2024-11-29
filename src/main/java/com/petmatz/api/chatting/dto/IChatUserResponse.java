package com.petmatz.api.chatting.dto;

import com.petmatz.domain.chatting.dto.IChatUserInfo;
import lombok.Builder;

@Builder
public record IChatUserResponse(

        String userId,
        String userName,

        String userEmail,
        String profileURL

) {

    public static IChatUserResponse of(IChatUserInfo iChatUserInfo) {
        return IChatUserResponse.builder()
                .userId(iChatUserInfo.userId())
                .userName(iChatUserInfo.userName())
                .userEmail(iChatUserInfo.userEmail())
                .profileURL(iChatUserInfo.profileURL())
                .build();
    }

}
