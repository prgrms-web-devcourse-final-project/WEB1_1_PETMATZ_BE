package com.petmatz.domain.petmission.dto;

import com.petmatz.api.petmission.dto.PetMissionCommentResponse;
import com.petmatz.domain.petmission.entity.PetMissionAnswerEntity;
import com.petmatz.domain.petmission.entity.PetMissionAskEntity;
import lombok.Builder;

@Builder
public record PetMissionAskInfo(

        Long id,
        String comment,
        String ask,
        String imgURL
) {

    public static PetMissionAskInfo of(PetMissionAskEntity petMissionAskEntity) {
        PetMissionAnswerEntity missionAnswer = petMissionAskEntity.getMissionAnswer();
        if (missionAnswer == null) {
            return PetMissionAskInfo.builder()
                    .id(petMissionAskEntity.getId())
                    .ask(petMissionAskEntity.getComment())
                    .build();
        }
        return PetMissionAskInfo.builder()
                .id(petMissionAskEntity.getId())
                .ask(petMissionAskEntity.getComment())
                .comment(missionAnswer.checkCommentNull())
                .imgURL(missionAnswer.checkURLNull())
                .build();
    }
}
