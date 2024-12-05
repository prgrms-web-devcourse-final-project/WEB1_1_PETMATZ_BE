package com.petmatz.domain.pet.dto;

import lombok.Builder;

@Builder
public record PetSaveResponse(

        Long UUID,

        String resultImgURL
) {

    public static PetSaveResponse of(Long UUID, String resultImgURL) {
        return PetSaveResponse.builder()
                .UUID(UUID)
                .resultImgURL(resultImgURL)
                .build();
    }

}
