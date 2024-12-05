package com.petmatz.domain.petmission.dto;

import com.petmatz.domain.pet.Gender;
import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.petmission.entity.PetToPetMissionEntity;
import lombok.Builder;

import java.util.List;

@Builder
public record PetMissionPetInfo(

        String petName,

        String breed,

        Integer age,

        Gender gender,

        String neuterYn
) {

    public static PetMissionPetInfo of(Pet pet) {
        return PetMissionPetInfo.builder()
                .petName(pet.getPetName())
                .breed(pet.getBreed())
                .age(pet.getAge())
                .gender(pet.getGender())
                .neuterYn(pet.getNeuterYn())
                .build();
    }

}
