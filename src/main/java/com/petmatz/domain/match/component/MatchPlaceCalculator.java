package com.petmatz.domain.match.component;

import com.petmatz.domain.match.dto.request.DistanceRequest;
import com.petmatz.domain.match.exception.MatchException;
import com.petmatz.domain.match.dto.response.UserResponse;
import com.petmatz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.petmatz.domain.match.exception.MatchErrorCode.*;

@Service
@Component
@RequiredArgsConstructor
public class MatchPlaceCalculator {
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

        return EARTH_RADIUS_KM * c; // km 단위로 반환
    }

    public double calculateDistanceOnly(User user, UserResponse targetUser) {
        double distance = calculateDistance(createDistanceRequest(user, targetUser));
        return Math.round(distance * 100.0) / 100.0;
    }

    public double findMatchesWithinDistance(User user, UserResponse targetUser) {
        double distanceScore = 0.0;

        if (user.getId() == null || targetUser.id() == null || user.getId().equals(targetUser.id())) {
            return distanceScore;
        }

        try {
            user.checkLatitudeLongitude();
            targetUser.checkTargetUserLatitudeLongitude();

            double distance = calculateDistance(createDistanceRequest(user, targetUser));
            distanceScore = calculateDistanceScore(distance);

        } catch (MatchException e) {
            throw new MatchException(INVALID_MATCH_DATA,
                    "Error for user " + user.getId() + " and target " + targetUser.id() + ": " + e.getMessage());
        }

        return distanceScore;
    }



    private DistanceRequest createDistanceRequest(User user, UserResponse targetUser) {
        return new DistanceRequest(
                (user.getLatitude()),
                (user.getLongitude()),
                (targetUser.latitude()),
                (targetUser.longitude())
        );
    }


    private double calculateDistanceScore(double distance) {
        if (distance <= 5.0) {
            return 70.0;
        } else if (distance <= 7.0) {
            return 62.0;
        } else if (distance <= 9.0) {
            return 53.0;
        } else if (distance <= 11.0) {
            return 45.0;
        } else if (distance <= 15.0) {
            return 40.0;
        } else if (distance <= 20.0) {
            return 35.0;
        } else if (distance <= 25.0) {
            return 30.0;
        } else if (distance <= 30.0) {
            return 26.0;
        } else if (distance <= 40.0) {
            return 15.0;
        } else {
            return 0.0;
        }
    }

}
