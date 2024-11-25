package com.petmatz.domain.match.dto.response;

public record MatchResultResponse(
        Long id,
        double distance,
        double distanceScore,
        double careAvailabilityScore,
        double sizePreferenceScore,
        double mbtiScore,
        double totalScore
) {
}
