package com.petmatz.domain.match.component;

import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.dto.response.UserResponse;
import com.petmatz.domain.pet.PetServiceImpl;
import com.petmatz.domain.user.entity.User;
import com.petmatz.infra.redis.component.RedisMatchComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MatchScoreCalculator {

    private final MatchPlaceCalculator matchPlaceCalculator;
    private final MatchCareCalculator matchCareCalculator;
    private final MatchSizeCalculator matchSizeCalculator;
    private final MatchMbtiCalculator matchMbtiCalculator;
    private final PetServiceImpl petService;
    private final RedisMatchComponent matchComponent;

    public MatchScoreResponse calculateScore(User user, UserResponse targetUser) {
        double distance = matchPlaceCalculator.calculateDistanceOnly(user, targetUser);
        double distanceScore = matchPlaceCalculator.findMatchesWithinDistance(user, targetUser);
        double careScore = matchCareCalculator.calculateCareScore(targetUser.isCareAvailable());
        double sizeScore = matchSizeCalculator.calculateDogSizeScore(user.getId(), targetUser.preferredSize());
        double mbtiScore = matchMbtiCalculator.calculateMbtiAverageScore(
                targetUser.mbti(), petService.getTemperamentsByUserId(user.getId()));

        double totalScore = distanceScore + careScore + sizeScore + mbtiScore;

        return new MatchScoreResponse(
                targetUser.id(),
                Math.round(distance * 100.0) / 100.0,
                Math.round(distanceScore * 100.0) / 100.0,
                Math.round(careScore * 100.0) / 100.0,
                Math.round(sizeScore * 100.0) / 100.0,
                Math.round(mbtiScore * 100.0) / 100.0,
                Math.round(totalScore * 100.0) / 100.0
        );
    }

    public List<MatchScoreResponse> decreaseScore(Long userId, Long targetId) {
        double penaltyScore = 60.0;
        List<MatchScoreResponse> matchScores = matchComponent.getMatchScores(userId);

        return matchScores.stream()
                .map(match -> {
                    if (match.id().equals(targetId)) {
                        double newScore = Math.max(0, match.totalScore() - penaltyScore);
                        return match.withUpdatedScore(newScore);
                    }
                    return match;
                })
                .toList();
    }
}
