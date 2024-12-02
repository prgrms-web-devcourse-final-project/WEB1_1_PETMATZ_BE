package com.petmatz.domain.match.dto.response;

public record DetailedMatchResultResponse(
        Long id,
        String nickname,
        String profileImg,
        Integer recommendationCount,
        String region,
        Integer careCompletionCount,
        String email
        // double distance,

) {}
