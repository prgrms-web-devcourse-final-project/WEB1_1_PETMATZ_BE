package com.petmatz.api.pet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.petmatz.domain.pet.dto.PetServiceDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PetApiRequest(
        String dogRegNo,
        String ownerNm
) {
    public static PetServiceDto toServiceDto(PetApiRequest request) {
        return new PetServiceDto(
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

