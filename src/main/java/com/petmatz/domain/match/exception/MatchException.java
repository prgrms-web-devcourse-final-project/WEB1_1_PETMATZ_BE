package com.petmatz.domain.match.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.DomainException;

public class MatchException extends DomainException {
    public MatchException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public MatchException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
