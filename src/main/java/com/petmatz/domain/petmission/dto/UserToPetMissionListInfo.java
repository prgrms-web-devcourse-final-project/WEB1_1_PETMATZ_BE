package com.petmatz.domain.petmission.dto;

import com.petmatz.common.constants.PetMissionStatusZip;
import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserToPetMissionListInfo(
        Long missionId,

        LocalDateTime petMissionStarted,

        LocalDateTime petMissionEnd,

        PetMissionStatusZip status,

        PetInfo petInfo

) {


    public static UserToPetMissionListInfo of(UserToPetMissionEntity userToPetMissionEntity) {
        PetMissionEntity petMission = userToPetMissionEntity.getPetMission();
        return UserToPetMissionListInfo.builder()
                .missionId(userToPetMissionEntity.getId())
                .petMissionStarted(petMission.getPetMissionStarted())
                .petMissionEnd(petMission.getPetMissionEnd())
                .status(petMission.getStatus())
                .petInfo(PetInfo.of(petMission.getPet()))
                .build();
    }
}
