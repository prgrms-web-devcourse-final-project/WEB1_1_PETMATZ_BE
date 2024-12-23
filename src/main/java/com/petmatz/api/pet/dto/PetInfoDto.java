package com.petmatz.api.pet.dto;

import com.petmatz.domain.pet.vo.PetInf;

public record PetInfoDto(
        String dogRegNo, // 동물등록번호
        String dogNm,    // 개 이름
        String sexNm,    // 성별
        String kindNm,   // 품종
        String neuterYn  // 중성화 여부 (Y/N)
) {
    public static PetInfoDto of(PetInf serviceDto) {
        return new PetInfoDto(
                serviceDto.dogRegNo(),
                serviceDto.petName(),
                serviceDto.gender(),
                serviceDto.breed(),
                serviceDto.neuterYn()
        );
    }
}