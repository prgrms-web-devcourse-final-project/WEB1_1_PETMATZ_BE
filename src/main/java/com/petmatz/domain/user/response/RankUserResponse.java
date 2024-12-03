package com.petmatz.domain.user.response;

public record RankUserResponse(
        Long userId,
        Long rank,
        String nickname,
        Integer recommendationCount,
        String profileImage
) {
}
