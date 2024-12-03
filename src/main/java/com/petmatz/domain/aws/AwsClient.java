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


    public String uploadImg(String base64EncodedData, String folderName, String fileName, String defaultFolder) {
        if (base64EncodedData.startsWith("profile")) {
            String fileKey = defaultFolder + "/" + base64EncodedData;
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileKey;
        }

        return s3Client.uploadFile(defaultFolder, base64EncodedData, folderName, fileName);
    }

}
