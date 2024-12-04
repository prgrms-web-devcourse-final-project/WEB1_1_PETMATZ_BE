package com.petmatz.domain.aws;

import java.io.InputStream;
import java.net.URL;

public interface S3Client {

    URL getPresignedURL(String folderName, String userName);


}
