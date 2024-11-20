package com.petmatz.match;

import com.petmatz.domain.match.service.MatchSizeService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DogSizeScoringTest {
    private final MatchSizeService scoring = new MatchSizeService();

    @Test
    public void testSingleSizePreferredSmallOrLarge() {
        // Case 1: 소형견만 선호하고 대상 강아지가 소형견일 경우
        List<String> preferredSizes = List.of("Small");
        String targetSize = "Small";
        double score = scoring.calculateDogSizeScore(preferredSizes, targetSize);
        assertEquals(20.0, score);

        // Case 2: 대형견만 선호하고 대상 강아지가 대형견일 경우
        preferredSizes = List.of("Large");
        targetSize = "Large";
        score = scoring.calculateDogSizeScore(preferredSizes, targetSize);
        assertEquals(20.0, score);
    }

    @Test
    public void testSingleSizePreferredMedium() {
        // Case: 중형견만 선호하고 대상 강아지가 중형견일 경우
        List<String> preferredSizes = List.of("Medium");
        String targetSize = "Medium";
        double score = scoring.calculateDogSizeScore(preferredSizes, targetSize);
        assertEquals(18.0, score);
    }

    @Test
    public void testTwoSizesPreferred() {
        // Case: 소형견과 중형견을 선호하고 대상 강아지가 소형견일 경우
        List<String> preferredSizes = List.of("Small", "Medium");
        String targetSize = "Small";
        double score = scoring.calculateDogSizeScore(preferredSizes, targetSize);
        assertEquals(16.0, score);

        // Case: 중형견과 대형견을 선호하고 대상 강아지가 대형견일 경우
        preferredSizes = List.of("Medium", "Large");
        targetSize = "Large";
        score = scoring.calculateDogSizeScore(preferredSizes, targetSize);
        assertEquals(16.0, score);
    }

    @Test
    public void testThreeSizesPreferred() {
        // Case: 모든 크기를 선호하고 대상 강아지가 중형견일 경우
        List<String> preferredSizes = List.of("Small", "Medium", "Large");
        String targetSize = "Medium";
        double score = scoring.calculateDogSizeScore(preferredSizes, targetSize);
        assertEquals(14.0, score);
    }

    @Test
    public void testNoMatch() {
        // Case: 소형견만 선호하고 대상 강아지가 중형견일 경우
        List<String> preferredSizes = List.of("Small");
        String targetSize = "Medium";
        double score = scoring.calculateDogSizeScore(preferredSizes, targetSize);
        assertEquals(0.0, score);

        // Case: 중형견만 선호하고 대상 강아지가 대형견일 경우
        preferredSizes = List.of("Medium");
        targetSize = "Large";
        score = scoring.calculateDogSizeScore(preferredSizes, targetSize);
        assertEquals(0.0, score);
    }
}

