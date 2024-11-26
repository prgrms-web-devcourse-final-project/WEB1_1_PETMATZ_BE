package com.petmatz.domain.pet.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.CustomException;

public class PetServiceException extends CustomException {

    public PetServiceException(BaseErrorCode errorCode) {
        super(errorCode, "SERVICE");
    }

    public PetServiceException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }
}

