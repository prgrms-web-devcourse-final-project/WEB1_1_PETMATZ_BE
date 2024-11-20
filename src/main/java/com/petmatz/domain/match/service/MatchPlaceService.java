package com.petmatz.domain.match.service;

import com.petmatz.domain.match.dto.request.DistanceRequest;
import com.petmatz.domain.match.dto.response.UserMatchResponse;
import com.petmatz.domain.match.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchPlaceService {
    private static final double EARTH_RADIUS_KM = 6371; // 지구 반지름 (km)
    private static final double MAX_DISTANCE_KM = 3.0; //
    private static final double REGION_SCORE = 30.0; // 지역 점수 비중 (30%)


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

    private double calculateDistanceScore(double distance) {
        if (distance <= 0.5) {
            return 30.0; // 0.0 ~ 0.5km
        } else if (distance <= 1.0) {
            return 28.0; // 0.5 ~ 1.0km
        } else if (distance <= 1.5) {
            return 25.0; // 1.0 ~ 1.5km
        } else if (distance <= 2.0) {
            return 20.0; // 1.5 ~ 2.0km
        } else if (distance <= 2.5) {
            return 18.0; // 2.0 ~ 2.5km
        } else if (distance <= 3.0) {
            return 15.0; // 2.5 ~ 3.0km
        } else { // 3km 이상이면 일단 10점만
            return 10.0;
        }
    }

    public List<UserMatchResponse> findMatchesWithinDistance(User user, List<User> allUsers) {
        List<UserMatchResponse> matches = new ArrayList<>();

        for (User targetUser : allUsers) {
            if (user.getId().equals(targetUser.getId())) continue;

            DistanceRequest distanceRequest = new DistanceRequest(
                    Double.parseDouble(user.getLatitude()),
                    Double.parseDouble(user.getLongitude()),
                    Double.parseDouble(targetUser.getLatitude()),
                    Double.parseDouble(targetUser.getLongitude())
            );

            double distance = calculateDistance(distanceRequest);

            double distanceScore = calculateDistanceScore(distance);

            matches.add(new UserMatchResponse(
                    targetUser.getId(),
                    Math.round(distance * 100.0) / 100.0,
                    Math.round(distanceScore * 100.0) / 100.0
            ));
        }
        return matches;
    }

}
