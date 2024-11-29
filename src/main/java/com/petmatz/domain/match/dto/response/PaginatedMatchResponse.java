package com.petmatz.domain.match.dto.response;

import java.util.List;

public record PaginatedMatchResponse(
        List<DetailedMatchResultResponse> matchResults,
        int totalPages
) {
}
