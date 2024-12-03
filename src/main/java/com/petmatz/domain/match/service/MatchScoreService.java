package com.petmatz.domain.match.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.domain.match.exception.MatchException;
import com.petmatz.domain.match.repo.MatchUserRepository;
import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.dto.response.UserResponse;
import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.pet.PetRepository;
import com.petmatz.domain.pet.PetService;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.petmatz.domain.match.exception.MatchErrorCode.INVALID_REDIS_DATA;
import static com.petmatz.domain.match.exception.MatchErrorCode.NULL_MATCH_DATA;

@Service
@RequiredArgsConstructor
public class MatchScoreService {

    private final MatchUserRepository matchUserRepository;
    private final MatchCareService matchCareService;
    private final MatchMbtiService matchMbtiService;
    private final MatchPlaceService matchPlaceService;
    private final MatchSizeService matchSizeService;
    private final PetService petService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final PetRepository petRepository;


    // sql에서 필터링 후 1000명 가져오기
    public List<UserResponse> getUsersWithinBoundingBox(Long userId) {
        // 임시로 사용
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음: " + userId));

        double userLat = user.getLatitude();
        double userLng = user.getLongitude();
        double rangeKm = 100.0;  // 대략 60km 정도 필터링

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
                    List<String> ListPreferredSize = changeList(preferredSize);
                    String mbti = (String) row[5];
                    double distance = (double) row[6];

                    return new UserResponse(id, latitude, longitude, isCareAvailable, ListPreferredSize, mbti, distance);
                })
                .sorted(Comparator.comparingDouble(UserResponse::distance)) // 거리로 정렬
                .limit(1000)
                .collect(Collectors.toList());

        return userResponses;
    }

    // TODO 현재는 임시로 유저, pet(mbti) 직접 조회  | 추후에 user, pet  패키지 구현 의뢰 예정

    public List<MatchScoreResponse> calculateTotalScore(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

        List<UserResponse> targetUsers = getUsersWithinBoundingBox(userId);

        // 자기 자신 제외
        List<UserResponse> filteredTargetUsers = targetUsers.stream()
                .filter(targetUser -> !targetUser.id().equals(userId))
                .collect(Collectors.toList());

        List<MatchScoreResponse> matchResults = filteredTargetUsers.stream().map(targetUser -> {
            double distance = matchPlaceService.calculateDistanceOnly(user, targetUser);
            double distanceScore = matchPlaceService.findMatchesWithinDistance(user, targetUser);
            double careScore = matchCareService.calculateCareScore(targetUser.isCareAvailable());

            List<String> preferredSizes = targetUser.preferredSize();
            double sizeScore = matchSizeService.calculateDogSizeScore(user.getId(), preferredSizes);

            String dogMbti = getTemperamentByUserId(userId);

            double mbtiScore = matchMbtiService.calculateMbtiScore(dogMbti, targetUser.mbti());
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
        }).collect(Collectors.toList());

        // 점수로 내림차순 (같으면 거리로 오름차순)
        matchResults.sort(Comparator
                .comparingDouble(MatchScoreResponse::totalScore)
                .thenComparingDouble(MatchScoreResponse::distance).reversed()
        );

        String redisKey = "matchResult:" + user.getId();
        redisTemplate.opsForValue().set(redisKey, matchResults);

        return matchResults;
    }

    public void decreaseScore(Long userId, Long targetId) {
        double penaltyScore = 30.0;  // 감소시킬 점수
        String redisKey = "matchResult:" + userId;

        // Redis에서 캐시된 데이터를 가져옴
        Object rawData = redisTemplate.opsForValue().get(redisKey);

        if (rawData == null) {
            throw new MatchException(NULL_MATCH_DATA);
        }

        List<MatchScoreResponse> matchScores;

        // Redis에서 가져온 데이터를 JSON 문자열로 처리
        try {
            if (rawData instanceof String) {
                ObjectMapper objectMapper = new ObjectMapper();
                matchScores = objectMapper.readValue((String) rawData, new TypeReference<List<MatchScoreResponse>>() {
                });
            } else if (rawData instanceof List) {
                matchScores = (List<MatchScoreResponse>) rawData;
            } else {
                throw new MatchException(INVALID_REDIS_DATA);
            }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON 데이터 처리 중 오류 발생", e);
        }

        // 점수 감소
        List<MatchScoreResponse> updatedResults = matchScores.stream()
                .map(match -> {
                    if (match.id().equals(targetId)) {
                        // targetId에 해당하는 사용자 점수만 감소
                        double newScore = Math.max(0, match.totalScore() - penaltyScore);
                        return new MatchScoreResponse(
                                match.id(),
                                match.distance(),
                                match.distanceScore(),
                                match.careAvailabilityScore(),
                                match.sizePreferenceScore(),
                                match.mbtiScore(),
                                newScore // 수정된 totalScore
                        );
                    }
                    return match; // 다른 값은 그대로 유지
                })
                .toList();

        // JSON 형식으로 직렬화 후 Redis에 저장
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String updatedData = objectMapper.writeValueAsString(updatedResults);
            redisTemplate.opsForValue().set(redisKey, updatedData);  // String으로 저장
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("JSON 직렬화 오류 발생", e);
        }
    }


    // TODO 펫 서비스쪽으로 옮겨야 함
    public String getTemperamentByUserId(Long userId) {
        // userId로 Pet 목록 조회
        List<Pet> pets = petRepository.findByUserId(userId);

        // 첫 번째 Pet의 temperament 반환, 없으면 "Unknown"
        if (!pets.isEmpty()) {
            return pets.get(0).getTemperament() != null ? pets.get(0).getTemperament() : "Unknown";
        }
        return "Unknown";
    }

    //
    public List<String> changeList(String preferredSizeString) {
        List<String> preferredSize = preferredSizeString == null || preferredSizeString.isEmpty()
                ? new ArrayList<>()
                : Arrays.asList(preferredSizeString.split(","));
        return preferredSize;
    }
}
