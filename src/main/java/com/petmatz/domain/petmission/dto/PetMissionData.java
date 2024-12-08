package com.petmatz.domain.petmission.dto;

import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import com.petmatz.domain.user.entity.User;
import lombok.Builder;

import java.util.List;

@Builder
public record PetMissionData(

        String chatRoomId,


        Long petMissionId
) {

    public static PetMissionData of(String chatRoomId, PetMissionEntity petMissionEntity) {
        return PetMissionData.builder()
                .chatRoomId(chatRoomId)
                .petMissionId(petMissionEntity.getId())
                .build();
    }

    public static PetMissionData of(String chatRoomId, String petMissionId) {
        return PetMissionData.builder()
                .chatRoomId(chatRoomId)
                .petMissionId(Long.valueOf(petMissionId))
                .build();
    }
}
