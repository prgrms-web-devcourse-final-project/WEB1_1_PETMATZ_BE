package com.petmatz.domain.chatting.dto;

import com.petmatz.domain.user.entity.User;
import lombok.Builder;

@Builder
public record IChatUserInfo(

        Long userId,
        String userEmail,

        String userName,

        String profileURL

) {

    public static IChatUserInfo of(User user) {
        return IChatUserInfo.builder()
                .userId(user.getId())
                .userEmail(user.getAccountId())
                .userName(user.getNickname())
                .profileURL(user.getProfileImg())
                .build();
    }

}
