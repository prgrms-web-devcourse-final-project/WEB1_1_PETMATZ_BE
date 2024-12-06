package com.petmatz.domain.aws;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public interface S3Client {

    URL getPresignedURL(String folderName, String userName, String standard,String subpath1);

    void deleteImg(List<String> keyList);
}
