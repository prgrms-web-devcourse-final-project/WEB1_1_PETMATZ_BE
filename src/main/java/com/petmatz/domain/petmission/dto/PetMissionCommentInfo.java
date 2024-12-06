package com.petmatz.domain.petmission.dto;

import lombok.Builder;

@Builder
public record PetMissionCommentInfo(

        String missionId,
        String comment,
        String imgURL

) {

}
