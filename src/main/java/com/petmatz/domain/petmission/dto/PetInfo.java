package com.petmatz.domain.petmission.dto;

import com.petmatz.domain.pet.entity.Pet;
import lombok.Builder;

@Builder
public record PetInfo(

        Long petId,
        String imgURL


) {
    public static PetInfo of(Pet pet) {
        return PetInfo.builder()
                .petId(pet.getId())
                .imgURL(pet.getProfileImg())
                .build();
    }
}
