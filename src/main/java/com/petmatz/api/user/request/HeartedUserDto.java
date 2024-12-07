package com.petmatz.api.user.request;

import com.petmatz.domain.user.constant.PreferredSize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HeartedUserDto {
    private Long myId;
    private Long heartedId;
    private String nickname;
    private String profileImg;
    private Boolean careAvailable;
    private List<PreferredSize> preferredSizes;
}