package com.petmatz.domain.petmission.dto;

import com.petmatz.domain.petmission.entity.PetMissionAnswerEntity;
import com.petmatz.domain.petmission.entity.PetMissionAskEntity;
import lombok.Builder;

@Builder
public record PetMissionAskInfo(

        String comment,
        String ask,
        String imgURL
) {

    public static PetMissionAskInfo of(PetMissionAskEntity petMissionAskEntity) {
        PetMissionAnswerEntity missionAnswer = petMissionAskEntity.getMissionAnswer();
        if (missionAnswer == null) {
            return PetMissionAskInfo.builder().ask(petMissionAskEntity.getComment()).build();
        }
        return PetMissionAskInfo.builder()
                .ask(petMissionAskEntity.getComment())
                .comment(missionAnswer.checkCommentNull())
                .imgURL(missionAnswer.checkURLNull())
                .build();
    }
}
