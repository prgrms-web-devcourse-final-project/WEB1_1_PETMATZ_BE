package com.petmatz.domain.petmission.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;

import static com.petmatz.common.constants.ErrorCode.DUPLIACTION;

@RequiredArgsConstructor
public enum PetMissionErrorCode implements BaseErrorCode {
    DUPLIACTION_PET_MISSION_ANSWER(DUPLIACTION, "ANSWER_409", "이미 답변이 있습니다."),
    ;

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }
}
