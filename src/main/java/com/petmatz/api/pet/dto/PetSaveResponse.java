package com.petmatz.api.pet.dto;

import com.petmatz.domain.pet.dto.PetSaveInfo;
import lombok.Builder;

@Builder
public record PetSaveResponse(

        Long UUID,

        String resultImgURL
) {

    public static PetSaveResponse of(PetSaveInfo petSaveInfo) {
        return PetSaveResponse.builder()
                .UUID(petSaveInfo.UUID())
                .resultImgURL(petSaveInfo.resultImgURL())
                .build();
    }

}
