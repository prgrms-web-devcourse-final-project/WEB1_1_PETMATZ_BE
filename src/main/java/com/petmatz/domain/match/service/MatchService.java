package com.petmatz.domain.match.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.domain.match.dto.response.DetailedMatchResultResponse;
import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.exception.MatchException;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.petmatz.domain.match.exception.MatchErrorCode.INVALID_REDIS_DATA;
import static com.petmatz.domain.match.exception.MatchErrorCode.NULL_MATCH_DATA;
import static com.petmatz.domain.sosboard.exception.SosBoardErrorCode.USER_NOT_FOUND;

/**
 *  실질적으로 프로필을 불러오는 클래스
 */
@Service
@RequiredArgsConstructor
public class MatchService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public List<DetailedMatchResultResponse> getPageUserDetailsFromRedis(Long userId, int page, int size) {
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

            // 페이징 처리
            int start = page * size;
            int end = Math.min(start + size, matchScores.size());
            if (start >= matchScores.size()) {
                return Collections.emptyList();
            }

            List<MatchScoreResponse> pagedMatchScores = matchScores.subList(start, end);

            return pagedMatchScores.stream()
                    .map(score -> {
                        User user = userRepository.findById(score.id())
                                .orElseThrow(() -> new MatchException(USER_NOT_FOUND, "해당 ID의 사용자를 찾을 수 없습니다: " + score.id()));
                        // 추후에 예외 교체
                        return new DetailedMatchResultResponse(
                                user.getId(),
                                user.getNickname(),
                                user.getProfileImg(),
                                user.getRecommendationCount(),
                                user.getRegion(),
                                user.getCareCompletionCount()
                        );
                    })
                    .toList();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON 데이터 처리 중 오류 발생", e);
        } catch (ClassCastException e) {
            throw new IllegalStateException("데이터 타입 캐스팅 오류 발생", e);
        }
    }
}
