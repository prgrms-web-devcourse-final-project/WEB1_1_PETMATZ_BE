package com.petmatz.domain.petmission.dto;

import lombok.Builder;

@Builder
public record PetMissionAskInfo(

        String title,
        String comment,
        String imgURL
) {
}
