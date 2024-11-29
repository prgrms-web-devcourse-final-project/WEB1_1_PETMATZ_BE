package com.petmatz.api.petmission.dto;

import com.petmatz.domain.petmission.dto.PetMissionUserInfo;
import lombok.Builder;

@Builder
public record PetMissionUser(

        Long userId,
        String userProfileURL
) {

    public static PetMissionUser of(PetMissionUserInfo petMissionUserInfo) {
        return PetMissionUser.builder()
                .userId(petMissionUserInfo.userId())
                .userProfileURL(petMissionUserInfo.userProfileURL())
                .build();
    }
}
