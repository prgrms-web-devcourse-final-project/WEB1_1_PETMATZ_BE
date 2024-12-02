package com.petmatz.domain.match.dto.response;

import java.util.List;

public record UserResponse (
        Long id,
        Double latitude,
        Double longitude,
        Boolean isCareAvailable,
        List<String> preferredSize,
        String mbti,
        double distance
){
}
