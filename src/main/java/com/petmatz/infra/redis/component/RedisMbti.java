package com.petmatz.infra.redis.component;

import com.petmatz.domain.match.exception.MatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.petmatz.domain.match.exception.MatchErrorCode.INVALID_REDIS_DATA;
import static com.petmatz.domain.match.exception.MatchErrorCode.TARGET_MBTI_EMPTY;

@RequiredArgsConstructor
@Component
public class RedisMbti {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveMbtiScores(String mbti, Map<String, Integer> scores) {
        scores.forEach((targetMbti, score) -> {
            redisTemplate.opsForHash().put("MBTI:" + mbti, targetMbti, score.toString());
        });
    }

    // 특정 mbti 의 모든 점수 조회
    public Map<Object, Object> getMbtiScores(String mbti) {
        return redisTemplate.opsForHash().entries("Mbti : " + mbti);
    }


    // redis 에서 점수를 가져오기
    public List<Double> getScores(String TargetMbti, List<String> myDogMbtiList) {

        if (myDogMbtiList == null || myDogMbtiList.isEmpty()) {
            throw new MatchException(TARGET_MBTI_EMPTY);
        }
        return myDogMbtiList.stream()
                .map(targetMbti -> {
                    String score = (String) redisTemplate.opsForHash().get("MBTI:" + TargetMbti, targetMbti);
                    if (score != null) {
                        return Double.parseDouble(score);
                    } else {
                        throw new MatchException(INVALID_REDIS_DATA);
                    }
                })
                .collect(Collectors.toList());
    }
}
