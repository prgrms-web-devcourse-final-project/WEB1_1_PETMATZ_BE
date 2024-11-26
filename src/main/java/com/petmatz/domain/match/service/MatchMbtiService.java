package com.petmatz.domain.match.service;

import com.petmatz.domain.match.exception.MatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

import static com.petmatz.domain.match.exception.MatchErrorCode.INSUFFICIENT_MBTI_DATA;

@Service
@RequiredArgsConstructor
public class MatchMbtiService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 점수 전체 가져오기
    public Map<String, Integer> getMbtiScore(String userMbti) {
        String redisKey = userMbti.toUpperCase();
        Map<Object, Object> scores = redisTemplate.opsForHash().entries(redisKey);

        if (scores.isEmpty()) {
            throw new MatchException(INSUFFICIENT_MBTI_DATA);
        }

        return scores.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> (String) e.getKey(),
                        e -> Integer.parseInt(e.getValue().toString())
                ));
    }

    // 특정 상대
    public Integer calculateMbtiScore(String userMbti, String targetMbti) {
        String redisKey = userMbti.toUpperCase();
        String fieldKey = targetMbti.toUpperCase();

        Object score = redisTemplate.opsForHash().get(redisKey, fieldKey);

        if (score == null) {
            throw new MatchException(INSUFFICIENT_MBTI_DATA);
        }

        return Integer.parseInt(score.toString());
    }
}
