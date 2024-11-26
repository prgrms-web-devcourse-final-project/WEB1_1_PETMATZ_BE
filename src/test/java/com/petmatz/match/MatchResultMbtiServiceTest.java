package com.petmatz.match;

import com.petmatz.domain.match.service.MatchMbtiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Nested
@ExtendWith(MockitoExtension.class)
class MatchResultMbtiServiceTest {
    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    private MatchMbtiService matchMbtiService;

    @BeforeEach
    void setUp() {
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        matchMbtiService = new MatchMbtiService(redisTemplate);
    }

    @Test
    @DisplayName("엠비티아이 정상 계산")
    public void testCalculateMbtiScore() {
        // Mock 데이터로 임시 데이터 삽입
        Mockito.when(hashOperations.get("INFP", "ENFJ")).thenReturn(20);

        Integer score = matchMbtiService.calculateMbtiScore("INFP", "ENFJ");

        assertEquals(20, score.intValue());
    }

    @Test
    @DisplayName("엠비티아이 예외 상황")
    public void testGetScoreForInvalidTargetMbti() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            matchMbtiService.calculateMbtiScore("INFP", "INVALID");
        });

        assertEquals("예외는 추후 처리", exception.getMessage());
    }
}
