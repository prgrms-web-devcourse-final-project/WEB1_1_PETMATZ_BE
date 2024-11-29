package com.petmatz.domain.user.response;

public record RankUserResponse(
        Long rank,
        String nickname,
        Integer recommendationCount,
        String profileImage
) {
}
