package com.petmatz.domain.chatting.dto;

import lombok.Builder;

@Builder
public record IChatUserInfo(
        String userEmail,

        String profileURL

) {

    public static IChatUserInfo of(String userEmail,String profileURL) {
        return IChatUserInfo.builder()
                .userEmail(userEmail)
                .profileURL(profileURL)
                .build();
    }

}
