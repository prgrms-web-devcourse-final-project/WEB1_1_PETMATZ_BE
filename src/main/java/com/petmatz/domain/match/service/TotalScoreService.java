package com.petmatz.domain.match.service;

import com.petmatz.domain.match.response.MatchResultResponse;
import com.petmatz.domain.match.entity.User;
import com.petmatz.domain.match.repo.MatchUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TotalScoreService {

    private final MatchUserRepository matchUserRepository;
    private final MatchCareService matchCareService;
    private final MatchMbtiService matchMbtiService;
    private final MatchPlaceService matchPlaceService;
    private final MatchSizeService matchSizeService;
    private static final double EARTH_RADIUS_KM = 6371;

    // sql에서 필터링 후 1000명 가져오기
    public List<User> getUsersWithinBoundingBox(User user, double radiusKm) {
        double userLat = Double.parseDouble(user.getLatitude());
        double userLng = Double.parseDouble(user.getLongitude());

        double latMin = userLat - (radiusKm / EARTH_RADIUS_KM);
        double latMax = userLat + (radiusKm / EARTH_RADIUS_KM);
        double lngMin = userLng - (radiusKm / (EARTH_RADIUS_KM * Math.cos(Math.toRadians(userLat))));
        double lngMax = userLng + (radiusKm / (EARTH_RADIUS_KM * Math.cos(Math.toRadians(userLat))));

        // SQL로 사각형 범위 내 사용자(러프하게) 가져오기
        List<Object[]> rawUsers = matchUserRepository.findUsersWithinBoundingBox(latMin, latMax, lngMin, lngMax);

        List<User> users = rawUsers.stream()
                .map(row -> new User(
                        ((Number) row[0]).longValue(),
                        ((Number) row[1]).doubleValue(),
                        ((Number) row[2]).doubleValue(),
                        (Boolean) row[3],
                        (String) row[4],
                        (String) row[5]
                ))
                .collect(Collectors.toList());

        return users;
    }


    public List<MatchResultResponse> calculateTotalScore(User user, double radiusKm) {
        List<User> targetUsers = getUsersWithinBoundingBox(user, radiusKm);

        List<MatchResultResponse> matchResults = new ArrayList<>();

        for (User targetUser : targetUsers) {
            double distanceScore = matchPlaceService.findMatchesWithinDistance(user, radiusKm, targetUsers);

            double careScore = matchCareService.calculateCareScore(targetUser.getIsCareAvailable());

            // 11/25 사용자와 연결된 펫의 크기를 타고 가야할것 같음.
//            double sizeScore = matchSizeService.calculateDogSizeScore("머지후 개발 ");
            double sizeScore = 10.0; // 임시 점수

            double mbtiScore = matchMbtiService.calculateMbtiScore(user.getMbti(), targetUser.getEmail());

            double totalScore = distanceScore + careScore + sizeScore + mbtiScore;

            matchResults.add(new MatchResultResponse(
                    targetUser.getId(),
                    Math.round(distanceScore * 100.0) / 100.0,
                    Math.round(distanceScore * 100.0) / 100.0,
                    Math.round(careScore * 100.0) / 100.0,
                    Math.round(sizeScore * 100.0) / 100.0,
                    Math.round(mbtiScore * 100.0) / 100.0,
                    Math.round(totalScore * 100.0) / 100.0
            ));
        }
        return matchResults;
    }
}
