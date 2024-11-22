package com.petmatz.api.pet;

import com.petmatz.domain.pet.PetServiceDto;

public record PetRequest(
        String dogRegNo,
        String ownerNm,
        String petName,
        String breed,
        String gender,
        String isNeutered,
        String size, // 사용자가 선택한 사이즈
        Integer age,
        String temperament,
        String preferredWalkingLocation,
        String profileImg,
        String comment
) {
    public static PetServiceDto toServiceDto(PetRequest request) {
    return new PetServiceDto(
            request.dogRegNo(),
            request.ownerNm(),
            request.petName(),
            request.breed(),
            request.gender(),
            request.isNeutered(),
            request.size(),
            request.age(),
            request.temperament(),
            request.preferredWalkingLocation(),
            request.profileImg(),
            request.comment()
    );
}}

