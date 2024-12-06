package com.petmatz.domain.match.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.domain.match.component.MatchScoreProcessor;
import com.petmatz.domain.match.component.UserMapper;
import com.petmatz.domain.match.dto.response.DetailedMatchResultResponse;
import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.dto.response.PaginatedMatchResponse;
import com.petmatz.domain.match.exception.MatchException;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import com.petmatz.infra.redis.component.RedisMatchComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.petmatz.domain.match.exception.MatchErrorCode.INVALID_REDIS_DATA;
import static com.petmatz.domain.match.exception.MatchErrorCode.NULL_MATCH_DATA;
import static com.petmatz.domain.sosboard.exception.SosBoardErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final RedisMatchComponent redisMatchComponent;
    private final MatchScoreProcessor matchScoreProcessor;
    private final UserMapper userMapper;

    public PaginatedMatchResponse getPageUserDetailsFromRedis(Long userId, int page, int size) {
        String redisKey = "matchResult:" + userId;

        List<MatchScoreResponse> matchScores = redisMatchComponent.getMatchScoresFromRedis(redisKey);

        matchScores = matchScoreProcessor.sortMatchScores(matchScores);

        List<MatchScoreResponse> pagedMatchScores = matchScoreProcessor.paginateMatchScores(matchScores, page, size);

        List<DetailedMatchResultResponse> detailedMatchResults = userMapper.mapToDetailedMatchResults(pagedMatchScores);

        long totalElements = matchScores.size();
        int totalPages = matchScoreProcessor.calculateTotalPages(totalElements, size);

        return new PaginatedMatchResponse(detailedMatchResults, totalPages);
    }
}

