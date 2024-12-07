package com.petmatz.domain.match.service;

import com.petmatz.api.global.dto.Response;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.match.component.BoundingBoxCalculator;
import com.petmatz.domain.match.component.MatchScoreCalculator;
import com.petmatz.domain.match.component.MatchScoreProcessor;
import com.petmatz.domain.match.component.UserMapper;
import com.petmatz.domain.match.dto.response.BoundingBoxResponse;
import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.dto.response.UserResponse;
import com.petmatz.domain.match.exception.MatchException;
import com.petmatz.domain.match.repo.MatchUserRepository;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import com.petmatz.infra.redis.component.RedisMatchComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchScoreService {

    private final MatchUserRepository matchUserRepository;
    private final UserRepository userRepository;
    private final MatchScoreCalculator matchScoreCalculator;
    private final RedisMatchComponent matchComponent;
    private final UserMapper userMapper;
    private final BoundingBoxCalculator boundingBoxCalculator;
    private final RedisMatchComponent redisMatchComponent;
    private final MatchScoreProcessor matchScoreProcessor;
    private final JwtExtractProvider jwtExtractProvider;


    // sql에서 필터링 후 1000명 가져오기
    public List<UserResponse> getUsersWithinBoundingBox(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음: " + userId));

        double userLat = user.getLatitude();
        double userLng = user.getLongitude();

        // Bounding Box 계산
        BoundingBoxResponse boundingBox = boundingBoxCalculator.calculateBoundingBox(userLat, userLng, 100.0); // 대략 60km 정도

        List<Object[]> rawUsers = matchUserRepository.findUsersWithinBoundingBox(
                userLng, userLat, boundingBox.minLat(), boundingBox.maxLat(),
                boundingBox.minLng(), boundingBox.maxLng()
        );

        return rawUsers.stream()
                .map(userMapper::mapToUserResponse) // UserMapper로 매핑
                .sorted(Comparator.comparingDouble(UserResponse::distance)) // 거리로 정렬
                .limit(1000) // 상위 1000명 제한
                .collect(Collectors.toList());
}


    public void calculateTotalScore() {
        Long userId = jwtExtractProvider.findIdFromJwt();
        String redisKey = "matchResult:" + userId;

        try {
            List<MatchScoreResponse> cachedResults = redisMatchComponent.getMatchScoresFromRedis(redisKey);
            if (cachedResults != null && !cachedResults.isEmpty()) {
                return;
            }
        } catch (MatchException e) {
            log.info("Redis에 데이터가 없습니다! 새로운 사용자로 간주되어 새로 측정합니다.");
        }


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

        List<UserResponse> targetUsers = getUsersWithinBoundingBox(userId);

        List<UserResponse> filteredTargetUsers = targetUsers.stream()
                .filter(targetUser -> !targetUser.id().equals(userId))
                .toList();

        List<MatchScoreResponse> matchResults = filteredTargetUsers.stream()
                .map(targetUser -> matchScoreCalculator.calculateScore(user, targetUser))
                .collect(Collectors.toCollection(ArrayList::new)); // 가변 리스트 생성!!!

        matchResults.sort(Comparator
                .comparingDouble(MatchScoreResponse::totalScore)
                .thenComparingDouble(MatchScoreResponse::distance)
                .reversed()
        );

        matchComponent.saveMatchScores(userId, matchResults);
    }


    public void decreaseScore(Long targetId) {
        Long userId = jwtExtractProvider.findIdFromJwt();
        List<MatchScoreResponse> updatedResults = matchScoreCalculator.decreaseScore(userId, targetId);
        matchComponent.saveMatchScores(userId, updatedResults);
    }
}
