//package com.petmatz.domain.aws;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AwsS3Service {
//
//    private final AwsClient awsClient;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucketName;
//
//    @Value("${cloud.aws.region.static}")
//    private String region;
//
//    public String uploadImage(String base64EncodedData, String folderName) {
//        awsClient.uploadImg();
//    }
//
//}
