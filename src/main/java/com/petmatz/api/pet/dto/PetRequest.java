package com.petmatz.api.pet.dto;

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
}

