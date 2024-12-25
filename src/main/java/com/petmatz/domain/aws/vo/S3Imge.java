package com.petmatz.domain.aws.vo;

import java.net.URL;

public record S3Imge(

        URL resultImg,
        String uploadURL

) {

    public static S3Imge of(URL resultImg, String uploadURL) {
        return new S3Imge(resultImg, uploadURL);
    }

    public String checkResultImg() {
        if (resultImg == null) {
            return "";
        }
        return resultImg.toString();
    }


}
