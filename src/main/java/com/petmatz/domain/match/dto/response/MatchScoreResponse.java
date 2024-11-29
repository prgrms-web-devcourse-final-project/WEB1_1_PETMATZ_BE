package com.petmatz.domain.match.dto.response;

public record MatchScoreResponse(
        Long id,
        double distance,
        double distanceScore,
        double careAvailabilityScore,
        double sizePreferenceScore,
        double mbtiScore,
        double totalScore
) {
}
