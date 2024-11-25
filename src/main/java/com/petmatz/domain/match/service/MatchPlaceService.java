package com.petmatz.domain.match.service;

import com.petmatz.domain.match.dto.request.DistanceRequest;
import com.petmatz.domain.match.dto.response.MatchResultResponse;
import com.petmatz.domain.match.dto.response.UserMatchResponse;
import com.petmatz.domain.match.entity.User;
import com.petmatz.domain.match.repo.MatchUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MatchPlaceService {
    private static final double EARTH_RADIUS_KM = 6371; // 지구 반지름 (km)

    public double calculateDistance(DistanceRequest request) {
        double lat1 = Math.toRadians(request.latitude1());
        double lon1 = Math.toRadians(request.longitude1());
        double lat2 = Math.toRadians(request.latitude2());
        double lon2 = Math.toRadians(request.longitude2());

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS_KM * c; // (km)
    }

    public double findMatchesWithinDistance(User user, double radiusKm, List<User> allUsers) {
        double totalScore = 0.0;

        for (User targetUser : allUsers) {
            if (user.getId().equals(targetUser.getId())) continue;

            // 거리 계산
            DistanceRequest distanceRequest = new DistanceRequest(
                    Double.parseDouble(user.getLatitude()),
                    Double.parseDouble(user.getLongitude()),
                    Double.parseDouble(targetUser.getLatitude()),
                    Double.parseDouble(targetUser.getLongitude())
            );

            double distance = calculateDistance(distanceRequest);

            // 점수 계산 (거리만 사용하여 점수 계산)
            if (distance <= radiusKm) {
                double distanceScore = calculateDistanceScore(distance); // 거리 기반 점수 계산
                totalScore += distanceScore; // 점수 합산
            }
        }

        return totalScore; // 최종 점수 반환
    }

    private double calculateDistanceScore(double distance) {
        if (distance <= 0.5) {
            return 40.0; // 0.0 ~ 0.5km
        } else if (distance <= 1.0) {
            return 38.0; // 0.5 ~ 1.0km
        } else if (distance <= 1.5) {
            return 35.0; // 1.0 ~ 1.5km
        } else if (distance <= 2.0) {
            return 30.0; // 1.5 ~ 2.0km
        } else if (distance <= 2.5) {
            return 28.0; // 2.0 ~ 2.5km
        } else if (distance <= 3.0) {
            return 25.0; // 2.5 ~ 3.0km
        } else { // 3km 이상이면 일단 20점만
            return 20.0;
        }
    }

}
