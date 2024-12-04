package com.petmatz.domain.aws;

import com.petmatz.common.security.utils.JwtExtractProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AwsClient{

    private final S3Client s3Client;

    private final JwtExtractProvider jwtExtractProvider;

    
    public String uploadImg(String defaultFolder, String standard) {
        if(defaultFolder.startsWith("profile")) return "default";

        String folderName = Prefix.returnKoreaName(standard);
        //userName의 경우 토큰에서 가져와서 사용해야함 UUID로
//        Long userName = jwtExtractProvider.findIdFromJwt();
        String userName = "tset";
        return s3Client.getPresignedURL(folderName,userName);
    }


}
