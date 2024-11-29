package com.petmatz.domain.petmission.dto;

import com.petmatz.domain.pet.Pet;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record PetInfo(

        // 멍뭉이 이름
        String petName,
        // 프로필 이미지 경로 또는 URL
        String profileImg,
        // 품종
        String breed,
        // 나이
        Integer age,
        // 성별
        String gender,
        // 중성화 여부
        String neuterYn,
        // 성격
        String temperament

) {
}
