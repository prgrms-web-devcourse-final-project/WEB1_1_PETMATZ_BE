package com.petmatz.domain.petmission.dto;

import com.petmatz.domain.user.entity.User;
import lombok.Builder;

import java.util.List;

@Builder
public record PetMissionData(

        String chatRoomId,
        List<Long> petMissionId
) {

    public static PetMissionData of(String chatRoomId, List<Long> petMissionId) {
        return PetMissionData.builder()
                .chatRoomId(chatRoomId)
                .petMissionId(petMissionId)
                .build();
    }
}
