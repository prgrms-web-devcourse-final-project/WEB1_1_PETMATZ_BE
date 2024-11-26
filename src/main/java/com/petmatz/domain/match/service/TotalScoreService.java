package com.petmatz.domain.match.service;

import com.petmatz.domain.match.repo.MatchUserRepository;
import com.petmatz.domain.match.response.MatchResultResponse;
import com.petmatz.domain.match.response.UserResponse;
import com.petmatz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TotalScoreService {

    private final MatchUserRepository matchUserRepository;
    private final MatchCareService matchCareService;
    private final MatchMbtiService matchMbtiService;
    private final MatchPlaceService matchPlaceService;
    private final RedisTemplate<String, Object> redisTemplate;


    static final double RANGE_KM = 10.0;

    // sql에서 필터링 후 1000명 가져오기
    public List<UserResponse> getUsersWithinBoundingBox(User user) {
        double userLat = user.getLatitude();
        double userLng = user.getLongitude();
        double rangeKm = 200.0;  // 필터링 반경 (200km)

        // 러프한 범위 작성
        double latChange = rangeKm / 111.0;  // 1도 = 111km
        double lngChange = rangeKm / (111.0 * Math.cos(Math.toRadians(userLat)));

        double minLat = userLat - latChange;
        double maxLat = userLat + latChange;
        double minLng = userLng - lngChange;
        double maxLng = userLng + lngChange;

        // Bounding Box로 후보자들을 먼저 필터링
        List<Object[]> rawUsers = matchUserRepository.findUsersWithinBoundingBox(userLng, userLat, minLat, maxLat, minLng, maxLng);

        List<UserResponse> userResponses = rawUsers.stream()
                .map(row -> {
                    long id = ((Number) row[0]).longValue();
                    double latitude = ((Number) row[1]).doubleValue();
                    double longitude = ((Number) row[2]).doubleValue();
                    boolean isCareAvailable = (Boolean) row[3];
                    String preferredSize = (String) row[4];
                    String mbti = (String) row[5];

                    // 거리 계산
                    double distance = (double) row[6];
                    System.out.println("User ID: " + id + ", Distance: " + distance + " meters");

                    return new UserResponse(id, latitude, longitude, isCareAvailable, preferredSize, mbti, distance);
                })
                .sorted(Comparator.comparingDouble(UserResponse::distance)) // 거리로 정렬
                .limit(1000)
                .collect(Collectors.toList());

        return userResponses;
    }


    public List<MatchResultResponse> calculateTotalScore(User user) {
        List<UserResponse> targetUsers = getUsersWithinBoundingBox(user);

        List<MatchResultResponse> matchResults = new ArrayList<>();

        for (UserResponse targetUser : targetUsers) {
            double distance = matchPlaceService.calculateDistanceOnly(user, targetUser);
            double distanceScore = matchPlaceService.findMatchesWithinDistance(user, targetUser);
            double careScore = matchCareService.calculateCareScore(targetUser.isCareAvailable());

            // 11/25 사용자와 연결된 펫의 크기를 타고 가야할것 같음.
//            double sizeScore = matchSizeService.calculateDogSizeScore("머지후 개발 ");
            double sizeScore = 10.0;

            double mbtiScore = matchMbtiService.calculateMbtiScore(user.getMbti(), targetUser.mbti());
            double totalScore = distanceScore + careScore + sizeScore + mbtiScore;

            MatchResultResponse result = new MatchResultResponse(
                    targetUser.id(),
                    Math.round(distance * 100.0) / 100.0,
                    Math.round(distanceScore * 100.0) / 100.0,
                    Math.round(careScore * 100.0) / 100.0,
                    Math.round(sizeScore * 100.0) / 100.0,
                    Math.round(mbtiScore * 100.0) / 100.0,
                    Math.round(totalScore * 100.0) / 100.0
            );

            matchResults.add(result);
        }

        String redisKey = "matchResult:" + user.getId();
        redisTemplate.opsForValue().set(redisKey, matchResults);  // json으로

        return matchResults;
    }

}
