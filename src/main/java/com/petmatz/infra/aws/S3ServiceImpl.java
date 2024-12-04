package com.petmatz.infra.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.petmatz.domain.aws.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class S3ServiceImpl implements S3Client {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;


    //URL 반환
    //해당 URL은 회원가입, 이미지 수정때도 같이 사용이 가능 할듯
    @Override
    public String getPresignedURL(String folderName, String userName) {
        String path = createPath(folderName, userName);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = makePresignedURL(bucketName, path);
        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    //URL 생성
    private GeneratePresignedUrlRequest makePresignedURL(String bucket, String path) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, path)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    //URL 유효기간 설정
    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private String createPath(String prefix, String userName) {
        return String.format("%s/%s", prefix ,userName);

    }
}
