package com.petmatz.api.pet;

import com.petmatz.domain.pet.PetServiceDto;

public record PetResponse(
        String dogRegNo,
        String dogNm,
        String sexNm,
        String kindNm,
        String neuterYn
) {
    public static PetResponse of(PetServiceDto serviceDto) {
        return new PetResponse(
                serviceDto.dogRegNo(),
                serviceDto.petName(),
                serviceDto.gender(),
                serviceDto.breed(),
                serviceDto.isNeutered()
        );
    }
}
