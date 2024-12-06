package com.petmatz.domain.pet.dto;

import lombok.Builder;

@Builder
public record PetUpdateResponse(

        String UUID,
        String imgURL

) {

    public static PetUpdateResponse of(String UUID, String imgURL) {
        return PetUpdateResponse.builder()
                .UUID(UUID)
                .imgURL(imgURL)
                .build();
    }

}
