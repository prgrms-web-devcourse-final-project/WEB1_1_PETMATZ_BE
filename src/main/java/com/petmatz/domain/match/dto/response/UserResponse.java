package com.petmatz.domain.match.dto.response;

import com.petmatz.domain.match.exception.MatchException;

import java.util.List;

import static com.petmatz.domain.match.exception.MatchErrorCode.INSUFFICIENT_TARGET_LATITUDE_DATA;
import static com.petmatz.domain.match.exception.MatchErrorCode.INSUFFICIENT_TARGET_LONGITUDE_DATA;

public record UserResponse (
        Long id,
        Double latitude,
        Double longitude,
        Boolean isCareAvailable,
        List<String> preferredSize,
        String mbti,
        double distance
){
    public void checkTargetUserLatitudeLongitude() {
        if (latitude == null) {
            throw new MatchException(INSUFFICIENT_TARGET_LATITUDE_DATA);
        }
        if (longitude == null) {
            throw new MatchException(INSUFFICIENT_TARGET_LONGITUDE_DATA);
        }
    }
}
