package com.petmatz.domain.aws;

import com.petmatz.common.security.utils.JwtExtractProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class AwsClient {

    private final S3Client s3Client;

    private final JwtExtractProvider jwtExtractProvider;

    @Value("${default-img-url}")
    private String DEFAULT_IMG_URL;


    public URL uploadImg(String userEmail, String defaultFolder, String standard) throws MalformedURLException {
        if (defaultFolder.isEmpty()) {
            //에러발생

        }
        if (defaultFolder.startsWith("profile")) return new URL(DEFAULT_IMG_URL + defaultFolder + ".svg");
        String folderName = Prefix.returnKoreaName(standard);
        return s3Client.getPresignedURL(folderName, userEmail);
    }


}
