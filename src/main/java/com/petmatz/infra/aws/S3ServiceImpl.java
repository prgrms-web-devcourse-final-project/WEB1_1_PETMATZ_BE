package com.petmatz.infra.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.petmatz.domain.aws.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public URL getPresignedURL(String folderName, String userName, String standard,String dogRegNo) {
        String path = createPath(folderName, userName, standard, dogRegNo);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = makePresignedURL(bucketName, path);
        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

    @Override
    public void deleteImg(List<String> keyList) {
        List<KeyVersion> keyVersions = keyList.stream()
                .map(KeyVersion::new)
                .collect(Collectors.toList());

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName)
                .withKeys(keyVersions);

        amazonS3Client.deleteObjects(deleteObjectsRequest);
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

    //이미지 사진 경로 제작
    private String createPath(String prefix, String userName, String standard, String dogRegNo) {
        if (standard.equals("PET_IMG")) {
            return String.format("%s/%s/%s", prefix, userName, (userName + "_" + dogRegNo));
        }
        if (standard.equals("CARE_HISTORY_IMG")) {
//            return String.format("%s/%s/%s", prefix, userName, (userName + "_" + dogRegNo));
        }
        return String.format("%s/%s", prefix ,userName);

    }
}
