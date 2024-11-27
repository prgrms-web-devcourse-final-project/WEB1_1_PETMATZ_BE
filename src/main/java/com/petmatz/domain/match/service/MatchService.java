package com.petmatz.domain.match.service;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.domain.match.dto.response.MatchResultResponse;
import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.exception.MatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.petmatz.domain.match.exception.MatchErrorCode.NULL_MATCH_DATA;

/**
 *  실질적으로 프로필을 불러오는 클래스
 */
@Service
@RequiredArgsConstructor
public class MatchService {

    private final RedisTemplate<String, Object> redisTemplate;

    public List<MatchResultResponse> getPageUserIdsFromRedis(Long userId, int page, int size) {
        String redisKey = "matchResult:" + userId;

        Object rawData = redisTemplate.opsForValue().get(redisKey);
        if (rawData == null) {
            throw new MatchException(NULL_MATCH_DATA);
        }
        try {
            List<MatchScoreResponse> matchScores;

            if (rawData instanceof String) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree((String) rawData);
                JsonNode arrayNode = jsonNode.get(1); // [...]]에서 두 번째 요소 가져올라고
                matchScores = objectMapper.convertValue(
                        arrayNode,
                        new com.fasterxml.jackson.core.type.TypeReference<List<MatchScoreResponse>>() {}
                );
            } else if (rawData instanceof List) {
                matchScores = (List<MatchScoreResponse>) rawData;
            } else {
                throw new RuntimeException();
            }

            List<MatchResultResponse> results = matchScores.stream()
                    .map(score -> new MatchResultResponse(score.id(), score.distance(), score.totalScore()))
                    .toList();

            // 페이징 처리
            int start = page * size;
            int end = Math.min(start + size, results.size());

            if (start >= results.size()) {
                return Collections.emptyList(); // 요청한 페이지가 범위를 초과한 경우 빈 리스트 반환
            }

            return results.subList(start, end);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 오류", e);
        } catch (ClassCastException e) {
            throw new RuntimeException("데이터 타입 캐스팅 오류", e);
        }
    }
}

}