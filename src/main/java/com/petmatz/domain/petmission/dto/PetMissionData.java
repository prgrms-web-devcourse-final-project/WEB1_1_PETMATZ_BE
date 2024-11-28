package com.petmatz.domain.petmission.dto;

import com.petmatz.domain.user.entity.User;
import lombok.Builder;

import java.util.List;

@Builder
public record PetMissionData(

        String chatRoomId,
        String petMissionId
) {

    public static PetMissionData of(String chatRoomId, String petMissionId) {
        return PetMissionData.builder()
                .chatRoomId(chatRoomId)
                .petMissionId(petMissionId)
                .build();
    }
}
