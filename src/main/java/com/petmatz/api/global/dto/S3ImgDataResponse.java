package com.petmatz.api.global.dto;

import com.petmatz.domain.global.S3ImgDataInfo;

public record S3ImgDataResponse(

        Long UUID,

        String resultImgURL

) {
    public static S3ImgDataResponse of(S3ImgDataInfo petSaveInfo) {
        return new S3ImgDataResponse(petSaveInfo.UUID(), petSaveInfo.resultImgURL());
    }

}
