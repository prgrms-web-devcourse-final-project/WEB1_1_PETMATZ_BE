package com.petmatz.domain.sosboard.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SosBoardErrorCode implements BaseErrorCode {
    BOARD_NOT_FOUND(404, "SOS_BOARD_404", "요청한 SOS 게시글을 찾을 수 없습니다"),
    INVALID_CATEGORY(400, "SOS_CATEGORY_400", "유효하지 않은 카테고리입니다"),
    UNAUTHORIZED_ACCESS(403, "SOS_UNAUTHORIZED_403", "해당 게시글에 대한 접근 권한이 없습니다"),
    DATABASE_ERROR(500, "SOS_DATABASE_500", "데이터베이스 처리 중 오류가 발생했습니다"),
    INVALID_INPUT(400, "SOS_INPUT_400", "입력값이 유효하지 않습니다"),
    USER_NOT_FOUND(404, "USER_404", "요청한 사용자를 찾을 수 없습니다");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }
}
