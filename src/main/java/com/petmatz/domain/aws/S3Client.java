package com.petmatz.domain.aws;

import java.io.InputStream;

public interface S3Client {

    String uploadFile(String defaultFolder,String base64EncodedData, String folderName, String fileName);
    void deleteFile(String key);

}
