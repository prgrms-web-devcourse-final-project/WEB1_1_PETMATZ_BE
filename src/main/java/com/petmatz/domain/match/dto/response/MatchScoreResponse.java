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
    public MatchScoreResponse withUpdatedScore(double newScore) {
        return new MatchScoreResponse(
                this.id,
                this.distance,
                this.distanceScore,
                this.careAvailabilityScore,
                this.sizePreferenceScore,
                this.mbtiScore,
                newScore
        );
    }
}
