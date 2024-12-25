package com.petmatz.domain.user.service;

import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import com.petmatz.domain.user.response.RankUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankServiceImpl implements RankService{

    private final UserRepository userRepository;
    private final JwtExtractProvider jwtExtractProvider;
    @Override
    public List<RankUserResponse> getTopRankings() {
        // 추천 수 기준 내림차순
        List<User> ranking = userRepository.findAll(Sort.by(Sort.Order.desc("recommendationCount")));

        return ranking.stream()
                .map(user -> new RankUserResponse(
                        user.getId(),
                        (long) (ranking.indexOf(user) + 1),
                        user.getNickname(),
                        user.getRecommendationCount(),
                        user.getProfileImg()
                ))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<RankUserResponse> getTopRankingsByRegion() {
        Long userId = jwtExtractProvider.findIdFromJwt();
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));

        Integer regionCode = currentUser.getRegionCode();

        List<User> usersByRegion = userRepository.findByRegionCodeOrderByRecommendationCountDesc(regionCode);

        return usersByRegion.stream()
                .limit(10)
                .map(user -> {
                    long rank = usersByRegion.indexOf(user) + 1;
                    String profileImg;
                    if (rank <= 3) {
                        profileImg = user.getProfileImg(); // 1~3등만 프로필 사진 포함
                    } else {
                        profileImg = null; // 4등부터는 프로필 사진 제외
                    }
                    return new RankUserResponse(
                            user.getId(),
                            rank,
                            user.getNickname(),
                            user.getRecommendationCount(),
                            profileImg
                    );
                })
                .collect(Collectors.toList());
    }
}
