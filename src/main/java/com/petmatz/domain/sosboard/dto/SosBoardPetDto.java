package com.petmatz.domain.sosboard.dto;

import com.petmatz.domain.pet.Pet;

public record SosBoardPetDto(
        Long id,
        String petName,
        String breed,
        String gender,
        String neuterYn, // 중성 여부
        String size,
        Integer age,
        String temperament,
        String preferredWalkingLocation,
        String profileImg,
        String comment
) {
    // 정적 팩토리 메서드: Pet → SosBoardPetDto
    public static SosBoardPetDto of(Pet pet) {
        return new SosBoardPetDto(
                pet.getId(),
                pet.getPetName(),
                pet.getBreed(),
                pet.getGender().toString(),
                pet.getNeuterYn(), // 중성 여부
                pet.getSize().toString(),
                pet.getAge(),
                pet.getTemperament(),
                pet.getPreferredWalkingLocation(),
                pet.getProfileImg(),
                pet.getComment()
        );
    }
}

