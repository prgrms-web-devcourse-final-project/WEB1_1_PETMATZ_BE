package com.petmatz.domain.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;

import static com.petmatz.common.constants.ErrorCode.DUPLICATION;

@RequiredArgsConstructor
public enum ChatErrorCode implements BaseErrorCode {

    CHAT_ROOM_DUPLICATION(DUPLICATION, "CHAT_409", "채팅방이 중복됩니다.")

    ;


    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }
}
