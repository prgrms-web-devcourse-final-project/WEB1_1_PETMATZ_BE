package com.petmatz.api.chatting.dto;

import com.petmatz.domain.chatting.dto.IChatUserInfo;
import com.petmatz.domain.user.info.UserInfo;
import lombok.Builder;

@Builder
public record IChatUserResponse(

        Long userId,
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

    public static IChatUserResponse of(UserInfo userInfo) {
        return IChatUserResponse.builder()
                .userId(userInfo.id())
                .userName(userInfo.nickname())
                .userEmail(userInfo.email())
                .profileURL(userInfo.profileImg())
                .build();
    }

}
