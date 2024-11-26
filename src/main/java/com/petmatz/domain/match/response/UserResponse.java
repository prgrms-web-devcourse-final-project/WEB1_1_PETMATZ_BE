package com.petmatz.domain.match.response;

public record UserResponse (
        Long id,
        Double latitude,
        Double longitude,
        Boolean isCareAvailable,
        String preferredSize,
        String mbti,
        double distance
){
}
