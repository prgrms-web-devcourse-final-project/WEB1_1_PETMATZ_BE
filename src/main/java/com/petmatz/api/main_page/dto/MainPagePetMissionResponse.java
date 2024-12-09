package com.petmatz.api.main_page.dto;

import com.petmatz.common.constants.PetMissionStatusZip;
import com.petmatz.domain.petmission.dto.RoleType;
import com.petmatz.domain.petmission.entity.PetMissionAskEntity;
import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import com.sun.tools.javac.Main;
import lombok.Builder;

import java.util.List;

@Builder
public record MainPagePetMissionResponse(

        Long userId,
        Long petMissionId,
        List<String> comment,
        PetMissionStatusZip status,
        RoleType roleType,
        List<MaingPagePetInfo> petInfoList

) {

    public static MainPagePetMissionResponse of(UserToPetMissionEntity userToPetMissionEntity) {
        PetMissionEntity petMission = userToPetMissionEntity.getPetMission();
        return MainPagePetMissionResponse.builder()
                .userId(userToPetMissionEntity.getUser().getId())
                .petMissionId(petMission.getId())
                .comment(petMission.getPetMissionAsks().stream().map(PetMissionAskEntity::getComment).toList())
                .status(petMission.getStatus())
                .roleType(userToPetMissionEntity.getRoleType())
                .petInfoList(petMission.getPetToPetMissions().stream().map(MaingPagePetInfo::of).toList())
                .build();
    }
}
