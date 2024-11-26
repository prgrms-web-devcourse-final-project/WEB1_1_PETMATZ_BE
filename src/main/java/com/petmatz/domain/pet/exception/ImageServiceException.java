package com.petmatz.domain.pet.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.CustomException;

public class ImageServiceException extends CustomException {

    public ImageServiceException(BaseErrorCode errorCode) {
        super(errorCode, "SERVICE");
    }

    public ImageServiceException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }
}

