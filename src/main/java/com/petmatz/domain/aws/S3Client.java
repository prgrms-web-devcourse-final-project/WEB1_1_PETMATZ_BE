package com.petmatz.domain.aws;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public interface S3Client {

    URL getPresignedURL(String folderName, String userName, String standard,String dogRegNo);

    void deleteImg(List<String> keyList);
}
