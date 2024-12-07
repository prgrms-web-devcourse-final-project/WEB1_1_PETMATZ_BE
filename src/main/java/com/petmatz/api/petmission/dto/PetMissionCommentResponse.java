package com.petmatz.api.petmission.dto;

import lombok.Builder;

@Builder
public record PetMissionCommentResponse(

        Long id,
        String comment,
        String imgURL

) {
}
