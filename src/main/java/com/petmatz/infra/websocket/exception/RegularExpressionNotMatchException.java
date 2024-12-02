package com.petmatz.infra.websocket.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.InfraException;

public class RegularExpressionNotMatchException extends InfraException {

    public static final InfraException EXCEPTION = new RegularExpressionNotMatchException();

    private RegularExpressionNotMatchException() {
        super(WebSocketErrorCode.REGULAR_EXPRESSION_NOT_MATCH);
    }
}
