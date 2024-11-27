package com.petmatz.domain.pet.dto;

import com.petmatz.api.pet.dto.PetRequest;
import com.petmatz.domain.pet.Pet;

public record PetServiceDto(
        Long id,
        String dogRegNo,
        String ownerNm,
        String petName,
        String breed,
        String gender,
        String neuterYn,
        String size,
        Integer age,
        String temperament,
        String preferredWalkingLocation,
        String profileImg,
        String comment
) {
    public static PetServiceDto of(PetRequest request) {
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

    public static PetServiceDto of(Pet pet) {
        return new PetServiceDto(
                pet.getId(),
                pet.getDogRegNo(),// Long → String 변환
                null, // ownerNm은 null 처리
                pet.getPetName(),
                pet.getBreed(),
                pet.getGender().toString(),
                pet.getNeuterYn(),
                pet.getSize().toString(),
                pet.getAge(),
                pet.getTemperament(),
                pet.getPreferredWalkingLocation(),
                pet.getProfileImg(),
                pet.getComment()
        );
    }
}

