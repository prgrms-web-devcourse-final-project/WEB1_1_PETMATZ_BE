package com.petmatz.domain.aws;

import com.petmatz.domain.aws.vo.S3Imge;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    public S3Imge UploadImg(String userEmail, String defaultFolder, String standard, String subpath1) throws MalformedURLException {
        if (defaultFolder.startsWith("profile")) {
            URL uploadURL = new URL(DEFAULT_IMG_URL  + defaultFolder + ".svg");
            return S3Imge.of(null, uploadURL.toString());
        }
        String folderName = Prefix.returnKoreaName(standard);
        URL resultURL = s3Client.getPresignedURL(folderName, userEmail, standard, subpath1);
        String uploadURL = resultURL.getProtocol() + "://" + resultURL.getHost() + resultURL.getPath();
        return S3Imge.of(resultURL, uploadURL);
    }

    //이미지 연쇄 삭제
    public void deleteImg(List<String> keyList) {
        s3Client.deleteImg(keyList);
    }


}
