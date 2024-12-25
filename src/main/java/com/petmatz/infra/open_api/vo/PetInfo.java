package com.petmatz.infra.open_api.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.petmatz.domain.pet.dto.OpenApiPetInfo;

public record PetInfo(
        @JsonProperty("dogRegNo")
        String dogRegNo,

        @JsonProperty("rfidCd")
        String rfidCd,

        @JsonProperty("dogNm")
        String dogNm,

        @JsonProperty("sexNm")
        String sexNm,

        @JsonProperty("kindNm")
        String kindNm,

        @JsonProperty("neuterYn")
        String neuterYn,

        @JsonProperty("orgNm")
        String orgNm,

        @JsonProperty("officeTel")
        String officeTel,

        @JsonProperty("aprGbNm")
        String aprGbNm
) {

        public OpenApiPetInfo of() {
                return OpenApiPetInfo.builder()
                        .dogRegNo(dogRegNo)
                        .rfidCd(rfidCd)
                        .dogNm(dogNm)
                        .sexNm(sexNm)
                        .kindNm(kindNm)
                        .neuterYn(neuterYn)
                        .orgNm(orgNm)
                        .officeTel(officeTel)
                        .aprGbNm(aprGbNm)
                        .build();
        }


}
