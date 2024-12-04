package com.petmatz.domain.aws;

import java.io.InputStream;

public interface S3Client {

    String getPresignedURL(String folderName, String userName);


}
