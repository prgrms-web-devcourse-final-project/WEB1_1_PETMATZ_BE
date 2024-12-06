package com.petmatz.domain.aws;

import com.petmatz.common.security.utils.JwtExtractProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AwsClient {

    private final S3Client s3Client;


    @Value("${default-img-url}")
    private String DEFAULT_IMG_URL;


    //이미지 업로드 URL 발급
    public URL uploadImg(String userEmail, String defaultFolder, String standard, String subpath1) throws MalformedURLException {
        if (defaultFolder.isEmpty()) {
            //에러발생

        }
        if (defaultFolder.startsWith("profile"))
            return new URL(DEFAULT_IMG_URL + standard + "/" + Prefix.returnKoreaName(standard));
        String folderName = Prefix.returnKoreaName(standard);
        return s3Client.getPresignedURL(folderName, userEmail, standard, subpath1);
    }

    //이미지 연쇄 삭제
    public void deleteImg(List<String> keyList) {
        s3Client.deleteImg(keyList);
    }


}
