package com.petmatz.infra.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.petmatz.domain.aws.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class S3ServiceImpl implements S3Client {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Override
    public String uploadFile(String base64EncodedData, String folderName, String fileName, String defaultFolder) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedData);
        String fileKey;
        try (InputStream inputStream = new ByteArrayInputStream(decodedBytes)) {
            // 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(decodedBytes.length);
            metadata.setContentType("image/jpeg");
            fileKey = defaultFolder  + "/" + folderName + "/" + fileName;
            // S3에 업로드
            amazonS3Client.putObject(bucketName, fileKey, inputStream, metadata);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileKey;
    }


    @Override
    public void deleteFile(String key) {

    }
}
