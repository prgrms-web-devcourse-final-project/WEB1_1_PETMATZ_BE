package com.petmatz.api.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HeartedUserDto {
    private Long myId;
    private Long heartedId;
    private String nickname;
    private String profileImg;
}