package com.petmatz.api.pet.dto;

import com.petmatz.domain.pet.dto.OpenApiPetInfo;
import lombok.Builder;

@Builder
public record PetInfoResponse(
        String dogRegNo, // 동물등록번호
        String dogNm,    // 개 이름
        String sexNm,    // 성별
        String kindNm,   // 품종
        String neuterYn  // 중성화 여부 (Y/N)
) {
    public static PetInfoResponse of(OpenApiPetInfo openApiPetInfo) {
        return PetInfoResponse.builder()
                .dogRegNo(openApiPetInfo.dogRegNo())
                .dogNm(openApiPetInfo.dogNm())
                .sexNm(openApiPetInfo.sexNm())
                .kindNm(openApiPetInfo.kindNm())
                .neuterYn(openApiPetInfo.neuterYn())
                .build();
    }
}