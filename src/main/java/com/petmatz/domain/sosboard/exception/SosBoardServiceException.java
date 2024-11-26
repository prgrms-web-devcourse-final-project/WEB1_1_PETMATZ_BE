package com.petmatz.domain.sosboard.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.CustomException;

public class SosBoardServiceException extends CustomException {

    public SosBoardServiceException(BaseErrorCode errorCode) {
        super(errorCode, "SERVICE");
    }

    public SosBoardServiceException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }
}