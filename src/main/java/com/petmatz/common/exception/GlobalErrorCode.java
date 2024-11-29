package com.petmatz.common.exception;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
    PERMISSION_DENIED(403, "PERMISSION_DENIED_401", "해당 API 권한이 없습니다");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }
}
