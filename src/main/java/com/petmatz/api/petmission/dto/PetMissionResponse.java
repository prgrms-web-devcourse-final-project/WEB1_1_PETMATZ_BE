package com.petmatz.api.petmission.dto;

import com.petmatz.domain.petmission.dto.PetMissionData;
import com.petmatz.domain.petmission.dto.PetMissionUserInfo;
import lombok.Builder;

import java.util.List;

@Builder
public record PetMissionResponse(

        String chatRoomId,

        Long petMissionId


) {

    public static PetMissionResponse of(PetMissionData petMissionData) {
        return PetMissionResponse.builder()
                .chatRoomId(petMissionData.chatRoomId())
                .petMissionId(petMissionData.petMissionId())
                .build();
    }
}
