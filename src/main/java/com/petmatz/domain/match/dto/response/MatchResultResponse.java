package com.petmatz.domain.match.dto.response;

public record MatchResultResponse(
        Long id,
        double distance,
        double totalScore

) {
}
