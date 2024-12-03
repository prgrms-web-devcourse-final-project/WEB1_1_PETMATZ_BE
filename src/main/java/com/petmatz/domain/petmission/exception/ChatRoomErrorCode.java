package com.petmatz.domain.petmission.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;

import static com.petmatz.common.constants.ErrorCode.NOT_FOUND;


@RequiredArgsConstructor
public enum ChatRoomErrorCode implements BaseErrorCode {
    NOT_FOUND_CHATROOM(NOT_FOUND, "TOKEN_404", "없는 채팅방 입니다."),
    ;

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }
}
