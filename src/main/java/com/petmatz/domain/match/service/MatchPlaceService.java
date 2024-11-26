package com.petmatz.domain.match.service;

import com.petmatz.api.match.request.DistanceRequest;
import com.petmatz.domain.match.exception.MatchException;
import com.petmatz.domain.match.response.UserResponse;
import com.petmatz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.petmatz.domain.match.exception.MatchErrorCode.*;
import static com.petmatz.domain.match.service.TotalScoreService.RANGE_KM;

@Service
@RequiredArgsConstructor
public class MatchPlaceService {
    private static final double EARTH_RADIUS_KM = 6371; // 지구 반지름 (km)

    public double calculateDistance(DistanceRequest request) {
        // 위도 및 경도 값
        double lat1 = Math.toRadians(request.latitude1()); // 라디안으로 변환
        double lon1 = Math.toRadians(request.longitude1()); // 라디안으로 변환
        double lat2 = Math.toRadians(request.latitude2()); // 라디안으로 변환
        double lon2 = Math.toRadians(request.longitude2()); // 라디안으로 변환

        // 위도 및 경도 차이 계산
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        // 디버깅을 위한 출력
        System.out.println("Latitude1: " + lat1 + " Longitude1: " + lon1);
        System.out.println("Latitude2: " + lat2 + " Longitude2: " + lon2);

        // Haversine 공식
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        // 지구 반지름을 곱하여 실제 거리 계산
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
            checkLatitudeLongitude(user, "User");
            System.out.println("user : " + user.getLatitude());
            checkTargetUserLatitudeLongitude(targetUser, "Target user");
            System.out.println("target user : " + targetUser.latitude());

            double distance = calculateDistance(createDistanceRequest(user, targetUser));
            System.out.println("distance : " + distance);

            distanceScore = calculateDistanceScore(distance);
            System.out.println("점수는 " + distanceScore);

        } catch (MatchException e) {
            throw new MatchException(INVALID_MATCH_DATA,
                    "Error for user " + user.getId() + " and target " + targetUser.id() + ": " + e.getMessage());
        }

        return distanceScore;
    }

    public void checkLatitudeLongitude(User user, String userType) {
        if (user.getLatitude() <= 0) {
            throw new MatchException(INSUFFICIENT_LATITUDE_DATA, userType);
        }
        if (user.getLongitude() <= 0) {
            if (user.getLatitude() == 0.0) {
                throw new MatchException(INSUFFICIENT_LATITUDE_DATA, userType);
            }
            if (user.getLongitude() == 0.0) {
                throw new MatchException(INSUFFICIENT_LONGITUDE_DATA, userType);
            }
        }
    }

    public void checkTargetUserLatitudeLongitude(UserResponse user, String userType) {
        if (user.latitude() == null) {
            throw new MatchException(INSUFFICIENT_LATITUDE_DATA, userType);
        }
        if (user.longitude() == null) {
            throw new MatchException(INSUFFICIENT_LONGITUDE_DATA, userType);
        }
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
        if (distance <= 3.0) {
            return 40.0;
        } else if (distance <= 4.0) {
            return 38.0;
        } else if (distance <= 5.5) {
            return 35.0;
        } else if (distance <= 7.0) {
            return 30.0;
        } else if (distance <= 8.5) {
            return 28.0;
        } else if (distance <= 10.0) {
            return 25.0;
        } else { // 3km 이상이면 일단 20점만
            return 20.0;
        }
    }

}
