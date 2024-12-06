package com.petmatz.domain.petmission.dto;

import lombok.Builder;

@Builder
public record PetMissionCommentInfo(

        String askId,
        String missionAskId,
        String comment,
        String imgURL

) {

}
