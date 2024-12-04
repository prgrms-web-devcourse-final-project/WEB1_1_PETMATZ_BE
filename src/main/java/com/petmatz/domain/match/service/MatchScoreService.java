package com.petmatz.domain.match.service;

import com.petmatz.domain.match.component.BoundingBoxCalculator;
import com.petmatz.domain.match.component.MatchScoreCalculator;
import com.petmatz.domain.match.component.UserMapper;
import com.petmatz.domain.match.dto.response.BoundingBoxResponse;
import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.dto.response.UserResponse;
import com.petmatz.domain.match.repo.MatchUserRepository;
import com.petmatz.domain.match.utils.MatchUtil;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import com.petmatz.infra.redis.component.RedisMatchComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchScoreService {

    private final MatchUserRepository matchUserRepository;
    private final UserRepository userRepository;
    private final MatchScoreCalculator matchScoreCalculator;
    private final RedisMatchComponent matchComponent;
    private final MatchUtil matchUtil;
    private final UserMapper userMapper;
    private final BoundingBoxCalculator boundingBoxCalculator;


    // sql에서 필터링 후 1000명 가져오기
    public List<UserResponse> getUsersWithinBoundingBox(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음: " + userId));

        double userLat = user.getLatitude();
        double userLng = user.getLongitude();

        // Bounding Box 계산
        BoundingBoxResponse boundingBox = boundingBoxCalculator.calculateBoundingBox(userLat, userLng, 100.0);

        List<Object[]> rawUsers = matchUserRepository.findUsersWithinBoundingBox(
                userLng, userLat, boundingBox.minLat(), boundingBox.maxLat(),
                boundingBox.minLng(), boundingBox.maxLng()
        );

        // 사용자 데이터를 UserResponse로 매핑
        return rawUsers.stream()
                .map(userMapper::mapToUserResponse) // UserMapper로 매핑
                .sorted(Comparator.comparingDouble(UserResponse::distance)) // 거리로 정렬
                .limit(1000) // 상위 1000명 제한
                .collect(Collectors.toList());
}

    // TODO 현재는 임시로 유저, pet(mbti) 직접 조회  | 추후에 user, pet  패키지 구현 의뢰 예정

    public List<MatchScoreResponse> calculateTotalScore(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

        List<UserResponse> targetUsers = getUsersWithinBoundingBox(userId);

        List<UserResponse> filteredTargetUsers = targetUsers.stream()
                .filter(targetUser -> !targetUser.id().equals(userId))
                .toList();

        List<MatchScoreResponse> matchResults = filteredTargetUsers.stream()
                .map(targetUser -> matchScoreCalculator.calculateScore(user, targetUser))
                .collect(Collectors.toCollection(ArrayList::new)); // 가변 리스트 생성


        matchResults.sort(Comparator
                .comparingDouble(MatchScoreResponse::totalScore)
                .thenComparingDouble(MatchScoreResponse::distance)
                .reversed()
        );

        matchComponent.saveMatchScores(userId, matchResults);

        return matchResults;
    }


    public void decreaseScore(Long userId, Long targetId) {
        List<MatchScoreResponse> matchScores = matchComponent.getMatchScores(userId);

        List<MatchScoreResponse> updatedResults = matchScores.stream()
                .map(match -> {
                    if (match.id().equals(targetId)) {
                        double newScore = Math.max(0, match.totalScore() - 30.0);
                        return match.withUpdatedScore(newScore);
                    }
                    return match;
                })
                .toList();
        matchComponent.saveMatchScores(userId, updatedResults);
    }
}
