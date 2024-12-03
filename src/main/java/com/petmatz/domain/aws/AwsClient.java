package com.petmatz.domain.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AwsClient{

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final static String DEFAULT_FOLDER_NAME = "기본이미지_폴더";

    public String uploadImg(String base64EncodedData, String folderName, String fileName) {
        if (base64EncodedData.startsWith("profile")) {
            String fileKey = DEFAULT_FOLDER_NAME + "/" + base64EncodedData;
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileKey + ".svg";
        }

        return s3Client.uploadFile(base64EncodedData, folderName, fileName);
    }

}
