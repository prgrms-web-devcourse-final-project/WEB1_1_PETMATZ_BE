package com.petmatz.match;

import com.petmatz.domain.match.service.MatchCareService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchCareServiceTest {
    private final MatchCareService matchCareService = new MatchCareService();

    @Test
    public void testCareAvailableTrue() {
        // Case: 돌봄 가능 (true)
        Boolean isCareAvailable = true;
        double score = matchCareService.calculateCareScore(isCareAvailable);
        assertEquals(20.0, score);
    }

    @Test
    public void testCareAvailableFalse() {
        // Case: 돌봄 불가능 (false)
        Boolean isCareAvailable = false;
        double score = matchCareService.calculateCareScore(isCareAvailable);
        assertEquals(10.0, score);
    }

    @Test
    public void testCareAvailableNull() {
        // Case: 돌봄 정보 없음 (null)
        Boolean isCareAvailable = null;
        double score = matchCareService.calculateCareScore(isCareAvailable);
        assertEquals(10.0, score);
    }
}

