package com.petmatz.domain.match.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.domain.match.dto.response.DetailedMatchResultResponse;
import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.dto.response.PaginatedMatchResponse;
import com.petmatz.domain.match.exception.MatchException;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.petmatz.domain.match.exception.MatchErrorCode.INVALID_REDIS_DATA;
import static com.petmatz.domain.match.exception.MatchErrorCode.NULL_MATCH_DATA;
import static com.petmatz.domain.sosboard.exception.SosBoardErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public PaginatedMatchResponse getPageUserDetailsFromRedis(Long userId, int page, int size) {
        String redisKey = "matchResult:" + userId;

        Object rawData = redisTemplate.opsForValue().get(redisKey);
        if (rawData == null) {
            throw new MatchException(NULL_MATCH_DATA);
        }

        try {
            List<MatchScoreResponse> matchScores;
            if (rawData instanceof String) {
                ObjectMapper objectMapper = new ObjectMapper();
                matchScores = objectMapper.readValue(
                        (String) rawData,
                        new TypeReference<List<MatchScoreResponse>>() {}
                );
            } else if (rawData instanceof List) {
                matchScores = (List<MatchScoreResponse>) rawData;
            } else {
                throw new MatchException(INVALID_REDIS_DATA);
            }

            // 점수로 내림차순 (같으면 거리로 오름차순)
            matchScores.sort(Comparator
                    .comparingDouble(MatchScoreResponse::totalScore)
                    .thenComparingDouble(MatchScoreResponse::distance).reversed()
            );

            // 페이징 처리
            int start = page * size;
            int end = Math.min(start + size, matchScores.size());
            if (start >= matchScores.size()) {
                return new PaginatedMatchResponse(new ArrayList<>(), 0); // 혹시 넘으면 빈 리스트 반환
            }

            List<MatchScoreResponse> pagedMatchScores = matchScores.subList(start, end);

            long totalElements = matchScores.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);

            List<DetailedMatchResultResponse> detailedMatchResults = pagedMatchScores.stream()
                    .map(score -> {
                        // 추후 예외 교체
                        User user = userRepository.findById(score.id())
                                .orElseThrow(() -> new MatchException(USER_NOT_FOUND, "해당 ID의 사용자를 찾을 수 없습니다: " + score.id()));
                        return new DetailedMatchResultResponse(
                                user.getId(),
                                user.getNickname(),
                                user.getProfileImg(),
                                user.getRecommendationCount(),
                                user.getRegion(),
                                user.getCareCompletionCount(),
                                user.getEmail()
                        );
                    })
                    .collect(Collectors.toList());

            return new PaginatedMatchResponse(detailedMatchResults, totalPages);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON 데이터 처리 중 오류 발생", e);
        } catch (ClassCastException e) {
            throw new IllegalStateException("데이터 타입 캐스팅 오류 발생", e);
        }
    }
}
