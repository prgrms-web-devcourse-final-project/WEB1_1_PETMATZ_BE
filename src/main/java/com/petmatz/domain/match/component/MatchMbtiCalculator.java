package com.petmatz.domain.match.component;

import com.petmatz.domain.match.exception.MatchException;
import com.petmatz.infra.redis.component.RedisMbti;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.petmatz.domain.match.exception.MatchErrorCode.INSUFFICIENT_MBTI_DATA;

@Service
@RequiredArgsConstructor
public class MatchMbtiCalculator {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMbti redisMbti;

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
    public double calculateMbtiAverageScore(String TargetMbti, List<String> myDogMbtiList) {
        List<Double> scores = redisMbti.getScores(TargetMbti, myDogMbtiList);

        double average = scores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow(() -> new MatchException(INSUFFICIENT_MBTI_DATA));

        BigDecimal roundedAverage = BigDecimal.valueOf(average)
                .setScale(1, RoundingMode.FLOOR);

        return roundedAverage.doubleValue();
    }
}
