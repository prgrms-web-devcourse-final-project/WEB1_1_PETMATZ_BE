package com.petmatz.domain.match.response;


public record UserMatchResponse (
        Long userId,        // 매칭 대상 사용자 ID
        double distance,    // 거리 (km)
        double totalScore   // 총 점수
){}
