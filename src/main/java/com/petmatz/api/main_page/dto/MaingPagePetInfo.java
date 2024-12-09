package com.petmatz.api.main_page.dto;

import com.petmatz.domain.pet.Gender;
import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.petmission.entity.PetToPetMissionEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

@Builder
public record MaingPagePetInfo(

        Long petId,
        String petName,
        String breed,
        Integer age,
        Gender gender,
        String neuterYn,
        String temperament,
        String profileImg

) {

    public static MaingPagePetInfo of(PetToPetMissionEntity pet) {
        Pet petInfo = pet.getPet();
        return MaingPagePetInfo.builder()
                .petId(petInfo.getId())
                .petName(petInfo.getPetName())
                .breed(petInfo.getBreed())
                .age(petInfo.getAge())
                .gender(petInfo.getGender())
                .neuterYn(petInfo.getNeuterYn())
                .temperament(petInfo.getTemperament())
                .profileImg(petInfo.getProfileImg())
                .build();
    }

}
