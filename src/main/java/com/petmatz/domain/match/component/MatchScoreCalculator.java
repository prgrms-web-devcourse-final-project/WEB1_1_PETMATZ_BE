package com.petmatz.domain.match.component;

import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.dto.response.UserResponse;
import com.petmatz.domain.pet.PetServiceImpl;
import com.petmatz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchScoreCalculator {

    private final MatchPlaceCalculator matchPlaceCalculator;
    private final MatchCareCalculator matchCareCalculator;
    private final MatchSizeCalculator matchSizeCalculator;
    private final MatchMbtiCalculator matchMbtiCalculator;
    private final PetServiceImpl petService;

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

}
