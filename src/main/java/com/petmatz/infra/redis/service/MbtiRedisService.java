package com.petmatz.infra.redis.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;


@RequiredArgsConstructor
@Service
public class MbtiRedisService {


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

    // 특정 MBTI와 타겟 MBTI 간 점수 조회 (실질적 로직)
    public Integer getScore(String mbti, String targetMbti) {
        String score = (String) redisTemplate.opsForHash().get("MBTI:" + mbti, targetMbti);
        if (score != null) {
            return Integer.parseInt(score);
        } else {
            return null;
        }
    }
}

