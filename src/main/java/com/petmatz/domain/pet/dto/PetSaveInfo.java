package com.petmatz.domain.pet.dto;

import lombok.Builder;

@Builder
public record PetSaveInfo(

        Long UUID,

        String resultImgURL

) {

    public static PetSaveInfo of(Long UUID, String resultImgURL) {
        return PetSaveInfo.builder()
                .UUID(UUID)
                .resultImgURL(resultImgURL)
                .build();
    }

}
