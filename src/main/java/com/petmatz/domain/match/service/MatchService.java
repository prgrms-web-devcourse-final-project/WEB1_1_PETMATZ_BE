package com.petmatz.domain.match.service;


import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.match.component.MatchScoreProcessor;
import com.petmatz.domain.match.component.UserMapper;
import com.petmatz.domain.match.dto.response.DetailedMatchResultResponse;
import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.dto.response.PaginatedMatchResponse;
import com.petmatz.infra.redis.component.RedisMatchComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final RedisMatchComponent redisMatchComponent;
    private final MatchScoreProcessor matchScoreProcessor;
    private final UserMapper userMapper;
    private final JwtExtractProvider jwtExtractProvider;

    public PaginatedMatchResponse getPageUserDetailsFromRedis(int page, int size) {
        Long userId = jwtExtractProvider.findIdFromJwt();
        String redisKey = "matchResult:" + userId;

        List<MatchScoreResponse> matchScores = redisMatchComponent.getMatchScoresFromRedis(redisKey);

        matchScores = matchScoreProcessor.filterAndSortScores(matchScores);

        List<MatchScoreResponse> pagedMatchScores = matchScoreProcessor.paginateMatchScores(matchScores, page, size);

        List<DetailedMatchResultResponse> detailedMatchResults = userMapper.mapToDetailedMatchResults(pagedMatchScores);

        long totalElements = matchScores.size();
        int totalPages = matchScoreProcessor.calculateTotalPages(totalElements, size);

        PaginatedMatchResponse paginatedMatchResponse = new PaginatedMatchResponse(detailedMatchResults, totalPages);
        if (paginatedMatchResponse.matchResults().isEmpty()) {
            throw new NotFoundException("매치 결과가 없습니다.");
        }

        return paginatedMatchResponse;
    }
}

