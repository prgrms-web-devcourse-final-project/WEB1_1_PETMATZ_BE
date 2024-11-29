package com.petmatz.domain.pet.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PetErrorCode implements BaseErrorCode {
    DOG_REG_NO_DUPLICATE(400, "DOG_REG_NO_DUPLICATE", "이미 등록된 동물 등록 번호입니다."),
    PET_NOT_FOUND(404, "PET_NOT_FOUND", "해당 반려동물을 찾을 수 없습니다."),
    FETCH_FAILED(500, "FETCH_FAILED", "반려동물 정보를 조회하는 데 실패했습니다."),
    UPDATE_FAILED(500, "UPDATE_FAILED", "반려동물 정보를 업데이트하는 데 실패했습니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }
}

