package com.petmatz.api.pet.dto;

import com.petmatz.domain.pet.Gender;
import com.petmatz.domain.pet.Size;


public record PetUpdateRequest(

        int age,
        String breed,
        String comment,
        Gender gender,
        String neuterYn,
        String petName,
        String profileImg,
        Size size, // 사용자가 선택한 사이즈
        String temperament

) {
}
