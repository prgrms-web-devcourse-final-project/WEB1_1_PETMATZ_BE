package com.petmatz.api.petmission.dto;

import com.petmatz.domain.petmission.dto.PetMissionData;
import lombok.Builder;

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
