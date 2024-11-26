package com.petmatz.domain.pet.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageErrorCode implements BaseErrorCode {
    FILE_UPLOAD_ERROR(500, "IMAGE_UPLOAD_500", "이미지 업로드 중 오류가 발생했습니다"),
    FILE_NOT_FOUND(404, "IMAGE_NOT_FOUND_404", "요청한 이미지 파일을 찾을 수 없습니다"),
    INVALID_FILE_FORMAT(400, "IMAGE_FORMAT_400", "지원하지 않는 파일 형식입니다"),
    FILE_RETRIEVE_ERROR(500, "IMAGE_RETRIEVE_500", "이미지를 로드하는 중 오류가 발생했습니다");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }
}

