package com.petmatz.domain.match.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MatchErrorCode implements BaseErrorCode {
    INSUFFICIENT_CARE_DATA(400, "INSUFFICIENT_CARE_DATA", "돌봄 여부가 없습니다."),
    INSUFFICIENT_MBTI_DATA(400, "INSUFFICIENT_MBTI_DATA", "MBTI 별 점수를 찾을 수 없습니다."),
    INSUFFICIENT_LATITUDE_DATA(400, "INSUFFICIENT_LATITUDE_DATA", "위도가 없습니다."),
    INSUFFICIENT_LONGITUDE_DATA(400, "INSUFFICIENT_LONGITUDE_DATA", "경도가 없습니다."),
    INVALID_MATCH_DATA(400, "INVALID_MATCH_DATA", "위도 또는 경도가 누락되었습니다."),
    NULL_PREFERRED_SIZES(400, "NULL_PREFERRED_SIZES", "선호 크기 목록이 없습니다."),
    NULL_TARGET_SIZE(400, "NULL_TARGET_SIZE", "타겟 크기가 없습니다."),
    NULL_MATCH_DATA(400, "NULL_MATCH_DATA", "redis에 저장된 데이터가 없습니다.");




    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }
}

