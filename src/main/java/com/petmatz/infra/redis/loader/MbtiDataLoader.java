package com.petmatz.infra.redis.loader;

import com.petmatz.infra.redis.service.MbtiRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

@Component
@RequiredArgsConstructor
public class MbtiDataLoader implements CommandLineRunner {

    private final MbtiRedisService mbtiRedisService;

    public void loadData() {
        // MBTI 궁합 점수 데이터
        Map<String, Map<String, Integer>> compatibilityMap = new HashMap<>();

        compatibilityMap.put("INFP", Map.ofEntries(
                entry("ESTP", 5), entry("ISTP", 5), entry("ESFP", 10),
                entry("ISFP", 10), entry("ISFJ", 10), entry("ESFJ", 10),
                entry("ISTJ", 10), entry("ESTJ", 10), entry("INTP", 15),
                entry("ENTP", 15), entry("INTJ", 15), entry("ENTJ", 15),
                entry("INFP", 20), entry("ENFP", 20), entry("INFJ", 20),
                entry("ENFJ", 20)
        ));

        compatibilityMap.put("ENFP", Map.ofEntries(
                entry("ESTP", 5), entry("ESFP", 5), entry("ISTP", 10),
                entry("ISFP", 10), entry("ISFJ", 10), entry("ESFJ", 10),
                entry("ISTJ", 10), entry("ESTJ", 10), entry("INTP", 15),
                entry("ENTP", 15), entry("INTJ", 15), entry("ENTJ", 15),
                entry("INFP", 20), entry("ENFP", 20), entry("INFJ", 20),
                entry("ENFJ", 20)
        ));

        compatibilityMap.put("INFJ", Map.ofEntries(
                entry("ISFJ", 5), entry("ESFJ", 5), entry("ISTJ", 5),
                entry("ESTJ", 5), entry("ISFP", 10), entry("ESFP", 10),
                entry("ISTP", 10), entry("ESTP", 10), entry("INTP", 15),
                entry("ENTP", 15), entry("INTJ", 15), entry("ENTJ", 15),
                entry("INFP", 20), entry("ENFP", 20), entry("INFJ", 20),
                entry("ENFJ", 20)
        ));

        compatibilityMap.put("ENFJ", Map.ofEntries(
                entry("ISFJ", 5), entry("ESFJ", 5), entry("ISTJ", 5),
                entry("ESTJ", 5), entry("ISFP", 10), entry("ESFP", 10),
                entry("ISTP", 10), entry("ESTP", 10), entry("INTP", 15),
                entry("ENTP", 15), entry("INTJ", 15), entry("ENTJ", 15),
                entry("INFP", 20), entry("ENFP", 20), entry("INFJ", 20),
                entry("ENFJ", 20)
        ));

        compatibilityMap.put("INTJ", Map.ofEntries(
                entry("ISFP", 5), entry("ESFP", 5), entry("ISTP", 5),
                entry("ESTP", 5), entry("ISFJ", 10), entry("ESFJ", 10),
                entry("ISTJ", 10), entry("ESTJ", 10), entry("INTP", 15),
                entry("ENTP", 15), entry("INTJ", 15), entry("ENTJ", 15),
                entry("INFP", 20), entry("ENFP", 20), entry("INFJ", 20),
                entry("ENFJ", 20)
        ));

        compatibilityMap.put("ENTJ", Map.ofEntries(
                entry("ISFP", 5), entry("ESFP", 5), entry("ISTP", 5),
                entry("ESTP", 5), entry("ISFJ", 10), entry("ESFJ", 10),
                entry("ISTJ", 10), entry("ESTJ", 10), entry("INTP", 15),
                entry("ENTP", 15), entry("INTJ", 15), entry("ENTJ", 15),
                entry("INFP", 20), entry("ENFP", 20), entry("INFJ", 20),
                entry("ENFJ", 20)
        ));

        compatibilityMap.put("INTP", Map.ofEntries(
                entry("ESTP", 5), entry("ISFP", 5), entry("ESFP", 5),
                entry("ISTP", 5), entry("ESFJ", 10), entry("ISTJ", 10),
                entry("ESTJ", 10), entry("ISFJ", 10), entry("INTJ", 15),
                entry("ENTJ", 15), entry("INTP", 15), entry("ENTP", 15),
                entry("ENFJ", 20), entry("INFP", 20), entry("ENFP", 20),
                entry("INFJ", 20)
        ));
        compatibilityMap.put("ENTP", Map.ofEntries(
                entry("ESTP", 5), entry("ISFP", 5), entry("ESFP", 5), entry("ISTP", 5),
                entry("ESFJ", 10), entry("ISTJ", 10), entry("ESTJ", 10), entry("ISFJ", 10),
                entry("INTJ", 15), entry("ENTJ", 15), entry("ENFJ", 20), entry("INTP", 15),
                entry("ENTP", 15), entry("INFP", 20), entry("ENFP", 20), entry("INFJ", 20)
        ));

        compatibilityMap.put("ISFP", Map.ofEntries(
                entry("INFP", 5), entry("INTP", 5), entry("ENTP", 5), entry("ENFP", 10),
                entry("INFJ", 10), entry("INTJ", 10), entry("ENTJ", 10), entry("ENFJ", 15),
                entry("ISFJ", 20), entry("ESFJ", 20), entry("ISTJ", 20), entry("ESTJ", 20),
                entry("ISFP", 20), entry("ESFP", 20), entry("ISTP", 20), entry("ESTP", 20)
        ));

        compatibilityMap.put("ESFP", Map.ofEntries(
                entry("ENFP", 5), entry("ENTP", 5), entry("INTP", 5), entry("INFP", 10),
                entry("INTJ", 10), entry("ENTJ", 10), entry("INFJ", 15), entry("ENFJ", 15),
                entry("ISFJ", 20), entry("ESFJ", 20), entry("ISTJ", 20), entry("ESTJ", 20),
                entry("ISFP", 20), entry("ESFP", 20), entry("ISTP", 20), entry("ESTP", 20)
        ));

        compatibilityMap.put("ISTP", Map.ofEntries(
                entry("INFP", 5), entry("INTP", 5), entry("ENFP", 10), entry("ENTP", 10),
                entry("INTJ", 10), entry("ENTJ", 10), entry("INFJ", 15), entry("ENFJ", 15),
                entry("ISFJ", 20), entry("ESFJ", 20), entry("ISTJ", 20), entry("ESTJ", 20),
                entry("ISFP", 20), entry("ESFP", 20), entry("ISTP", 20), entry("ESTP", 20)
        ));

        compatibilityMap.put("ESTP", Map.ofEntries(
                entry("ENFP", 5), entry("ENTP", 5), entry("INTP", 5), entry("ENTJ", 5),
                entry("INFP", 10), entry("INFJ", 15), entry("ENFJ", 15), entry("ISFJ", 20),
                entry("ESFJ", 20), entry("ISTJ", 20), entry("ESTJ", 20), entry("ISFP", 20),
                entry("ESFP", 20), entry("ISTP", 20), entry("ESTP", 20)
        ));

        compatibilityMap.put("ISFJ", Map.ofEntries(
                entry("INFJ", 5), entry("INTJ", 5), entry("ENFJ", 5), entry("INFP", 5),
                entry("INTP", 10), entry("ENTJ", 10), entry("ENTP", 10), entry("ENFP", 15),
                entry("ISFJ", 15), entry("ISTJ", 15), entry("ESTJ", 15), entry("ISFP", 20),
                entry("ESFP", 20), entry("ISTP", 20), entry("ESTP", 20), entry("ESFJ", 20)
        ));

        compatibilityMap.put("ESFJ", Map.ofEntries(
                entry("ENFJ", 5), entry("ENTJ", 5), entry("INFJ", 10), entry("ENFP", 10),
                entry("INFP", 15), entry("INTP", 15), entry("ENTP", 15), entry("INTJ", 15),
                entry("ISFJ", 20), entry("ESFJ", 20), entry("ISTJ", 20), entry("ESTJ", 20),
                entry("ISFP", 20), entry("ESFP", 20), entry("ISTP", 20), entry("ESTP", 20)
        ));

        compatibilityMap.put("ISTJ", Map.ofEntries(
                entry("INFP", 0), entry("ENFP", 0), entry("INFJ", 0), entry("ENFJ", 0),
                entry("INTJ", 0), entry("ENTJ", 0), entry("INTP", 0), entry("ENTP", 0),
                entry("ISFJ", 10), entry("ESFJ", 10), entry("ISTJ", 10), entry("ESTJ", 10),
                entry("ISFP", 15), entry("ISTP", 15), entry("ESFP", 20), entry("ESTP", 20)
        ));

        compatibilityMap.put("ESTJ", Map.ofEntries(
                entry("ENFJ", 0), entry("ENTJ", 5), entry("ENFP", 5), entry("INFJ", 5),
                entry("INFP", 5), entry("INTJ", 10), entry("INTP", 10), entry("ENTP", 10),
                entry("ESFP", 15), entry("ESTP", 15), entry("ISFJ", 20), entry("ISFP", 20),
                entry("ISTJ", 20), entry("ISTP", 20), entry("ESFJ", 20), entry("ESTJ", 20)
        ));

        // Redis에 저장
        compatibilityMap.forEach((mbti, scores) -> mbtiRedisService.saveMbtiScores(mbti, scores));
    }

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }
}
