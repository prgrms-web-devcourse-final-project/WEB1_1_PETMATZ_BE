package com.petmatz.api.global.dto;

import com.petmatz.domain.global.S3ImgDataInfo;
import lombok.Builder;

@Builder
public record S3ImgDataResponse(

        Long UUID,

        String resultImgURL

) {
    public static S3ImgDataResponse of(S3ImgDataInfo petSaveInfo) {
        return S3ImgDataResponse.builder()
                .UUID(petSaveInfo.UUID())
                .resultImgURL(petSaveInfo.resultImgURL())
                .build();
    }

}
