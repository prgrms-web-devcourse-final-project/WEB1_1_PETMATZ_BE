package com.petmatz.domain.chatting.dto;

import lombok.Builder;

@Builder
public record IChatUserInfo(

        String userId,
        String userEmail,

        String userName,

        String profileURL

) {

    public static IChatUserInfo of(String userEmail,String profileURL) {
        return IChatUserInfo.builder()
                .userId()
                .userEmail(userEmail)
                .userName()
                .profileURL(profileURL)
                .build();
    }

}
