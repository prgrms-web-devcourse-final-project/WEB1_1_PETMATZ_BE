package com.petmatz.api.pet.dto;

import com.petmatz.domain.pet.dto.PetInf;

public record PetApiRequest(
        String dogRegNo,
        String ownerNm
) {
    public static PetInf toServiceDto(PetApiRequest request) {
        return new PetInf(
                null,
                request.dogRegNo(),
                request.ownerNm(),
                null, // 기본값 설정
                null, // 기본값 설정
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}

