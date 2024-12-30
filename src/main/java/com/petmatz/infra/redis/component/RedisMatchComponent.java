package com.petmatz.infra.redis.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.exception.MatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

import static com.petmatz.domain.match.exception.MatchErrorCode.INVALID_REDIS_DATA;
import static com.petmatz.domain.match.exception.MatchErrorCode.NULL_MATCH_DATA;

@Component
@RequiredArgsConstructor
public class RedisMatchComponent {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public List<MatchScoreResponse> getMatchScoresFromRedis(String redisKey) {
        Object rawData = redisTemplate.opsForValue().get(redisKey);
        if (rawData == null) {
            throw new MatchException(NULL_MATCH_DATA);
        }

        try {
            if (rawData instanceof String) {
                return objectMapper.readValue(
                        (String) rawData,
                        new TypeReference<List<MatchScoreResponse>>() {}
                );
            } else if (rawData instanceof List) {
                return (List<MatchScoreResponse>) rawData;
            } else {
                throw new MatchException(INVALID_REDIS_DATA);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json 파싱 중 에러발생 " + e);
        }
    }

    public List<MatchScoreResponse> getMatchScores(Long userId) {
        String redisKey = "matchResult:" + userId;
        Object rawData = redisTemplate.opsForValue().get(redisKey);

        if (rawData == null) {
            throw new MatchException(NULL_MATCH_DATA);
        }

        try {
            if (rawData instanceof String) {
                return objectMapper.readValue(
                        (String) rawData,
                        new TypeReference<List<MatchScoreResponse>>() {}
                );
            } else if (rawData instanceof List) {
                return (List<MatchScoreResponse>) rawData;
            } else {
                throw new MatchException(INVALID_REDIS_DATA);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류 발생", e);
        }
    }

    public void saveMatchScores(Long userId, List<MatchScoreResponse> matchScores) {
        String redisKey = "matchResult:" + userId;
        try {
            String data = objectMapper.writeValueAsString(matchScores);
            redisTemplate.opsForValue().set(redisKey, data, Duration.ofDays(30));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Redis 저장 중 JSON 직렬화 오류 발생", e);
        }
    }
}
