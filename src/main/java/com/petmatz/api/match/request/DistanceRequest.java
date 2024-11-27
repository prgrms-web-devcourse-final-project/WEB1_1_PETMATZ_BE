package com.petmatz.api.match.request;

public record DistanceRequest(
        double latitude1,  // 기준 사용자의 위도
        double longitude1, // 기준 사용자의 경도
        double latitude2,  // 대상 사용자의 위도
        double longitude2  // 대상 사용자의 경도
) {}
