package com.petmatz.domain.petmission.dto;

import com.petmatz.domain.petmission.entity.PetMissionAskEntity;
import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
public record PetMissionListInfo(

        Long id,

        LocalDateTime petMissionStarted,

        LocalDateTime petMissionEnd,
        PetMissionStatusZip status,


        List<PetMissionAskEntity> petMissionAsks

) {
    public static PetMissionListInfo of(PetMissionEntity petMission) {
        return PetMissionListInfo.builder()
                .id(petMission.getId())
                .petMissionStarted(petMission.getPetMissionStarted())
                .petMissionEnd(petMission.getPetMissionEnd())
                .status(petMission.getStatus())
                .petMissionAsks(petMission.getPetMissionAsks().stream()
                        .map(petMissionAskEntity -> PetMissionAskEntity.of(petMissionAskEntity.getComment())).toList())
                .build();
    }
}
