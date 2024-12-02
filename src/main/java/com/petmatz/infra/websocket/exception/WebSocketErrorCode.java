package com.petmatz.infra.websocket.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.petmatz.common.constants.ErrorCode.BAD_REQUEST;

@RequiredArgsConstructor
public enum WebSocketErrorCode implements BaseErrorCode {

    REGULAR_EXPRESSION_NOT_MATCH(BAD_REQUEST, "TOKEN_400", "지원 하지 않은 토큰"),
    SUBSCRIPTION_URL_NOT_FOUNT(HttpStatus.NOT_FOUND.value(), "TOKEN_400", "지원 하지 않은 토큰")

    ;

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }
}
