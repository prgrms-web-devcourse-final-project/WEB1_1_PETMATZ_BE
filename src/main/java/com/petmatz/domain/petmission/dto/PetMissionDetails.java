package com.petmatz.domain.petmission.dto;

import com.petmatz.common.constants.PetMissionStatusZip;
import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PetMissionDetails(

        Long id,
        String careName,
        String careEmail,
        Long careId,
        String receiverName,
        String receiverEmail,
        Long receiverId,
        LocalDateTime receiverStart,
        LocalDateTime receiverEnd,
        PetMissionStatusZip status,
        List<PetMissionPetInfo> petMissionPetInfos,
        List<PetMissionAskInfo> petMissionAskInfos

) {

    public static PetMissionDetails of(PetMissionEntity petMissionEntity, List<UserToPetMissionEntity> userToPetMissionEntities) {
        return PetMissionDetails.builder()
                .id(petMissionEntity.getId())
                .careName(userToPetMissionEntities.get(0).getUser().getNickname())
                .careEmail(userToPetMissionEntities.get(0).getUser().getAccountId())
                .careId(userToPetMissionEntities.get(0).getUser().getId())
                .receiverName(userToPetMissionEntities.get(1).getUser().getNickname())
                .receiverEmail(userToPetMissionEntities.get(1).getUser().getAccountId())
                .receiverId(userToPetMissionEntities.get(1).getUser().getId())
                .receiverStart(petMissionEntity.getPetMissionStarted())
                .receiverEnd(petMissionEntity.getPetMissionEnd())
                .petMissionPetInfos(petMissionEntity.getPetToPetMissions().stream().map(pet ->
                        PetMissionPetInfo.of(pet.getPet())).toList()
                )
                .status(petMissionEntity.getStatus())
                .petMissionAskInfos(petMissionEntity.getPetMissionAsks().stream().map(PetMissionAskInfo::of).toList())
                .build();
    }

}
