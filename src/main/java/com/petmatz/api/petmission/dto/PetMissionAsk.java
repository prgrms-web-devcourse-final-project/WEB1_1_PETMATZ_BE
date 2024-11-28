package com.petmatz.api.petmission.dto;

import com.petmatz.domain.petmission.dto.PetMissionAskInfo;
import lombok.Builder;


public record PetMissionAsk(

        String title,
        String comment,
        String imgURL

) {

    public PetMissionAskInfo of() {
        return PetMissionAskInfo.builder()
                .comment(comment)
                .build();
    }

}
