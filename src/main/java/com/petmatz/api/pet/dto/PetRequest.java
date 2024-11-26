package com.petmatz.api.pet.dto;

import com.petmatz.domain.pet.dto.PetServiceDto;

public record PetRequest(
        Long id,
        String dogRegNo,
        String ownerNm,
        String petName,
        String breed,
        String gender,
        String neuterYn,
        String size, // 사용자가 선택한 사이즈
        Integer age,
        String temperament,
        String preferredWalkingLocation,
        String profileImg,
        String comment
) {
    public static PetServiceDto toServiceDto(PetRequest request) {
        return new PetServiceDto(
                request.id(),
                request.dogRegNo(),
                request.ownerNm(),
                request.petName(),
                request.breed(),
                request.gender(),
                request.neuterYn(),
                request.size(),
                request.age(),
                request.temperament(),
                request.preferredWalkingLocation(),
                request.profileImg(),
                request.comment()
        );
    }
}

