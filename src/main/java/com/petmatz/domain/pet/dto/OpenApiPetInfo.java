package com.petmatz.domain.pet.dto;

import lombok.Builder;

@Builder
public record OpenApiPetInfo(

        String dogRegNo,

        String rfidCd,

        String dogNm,

        String sexNm,

        String kindNm,

        String neuterYn,

        String orgNm,

        String officeTel,

        String aprGbNm

) {

}
