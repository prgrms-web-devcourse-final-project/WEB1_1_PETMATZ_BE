package com.petmatz.domain.petmission.dto;

import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PetMissionDetails(

    Long id,
    String careName,
    String receiverName,
    LocalDateTime receiverStart,
    LocalDateTime receiverEnd,
    List<PetMissionPetInfo> petMissionPetInfos,
    List<PetMissionAskInfo> petMissionAskInfos

) {

    public static PetMissionDetails of(PetMissionEntity petMissionEntity, List<UserToPetMissionEntity> userToPetMissionEntities) {
        return PetMissionDetails.builder()
                .id(petMissionEntity.getId())
                .careName(userToPetMissionEntities.get(0).getUser().getNickname())
                .receiverName(userToPetMissionEntities.get(1).getUser().getNickname())
                .receiverStart(petMissionEntity.getPetMissionStarted())
                .receiverEnd(petMissionEntity.getPetMissionEnd())
                .petMissionPetInfos(petMissionEntity.getPetToPetMissions().stream().map(pet ->
                        PetMissionPetInfo.of(pet.getPet())).toList()
                )
                .petMissionAskInfos(petMissionEntity.getPetMissionAsks().stream().map(PetMissionAskInfo::of).toList())
                .build();
    }

}
