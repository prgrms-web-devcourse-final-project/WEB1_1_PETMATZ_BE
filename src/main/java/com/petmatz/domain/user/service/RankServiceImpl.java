package com.petmatz.domain.user.service;

import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import com.petmatz.domain.user.response.RankUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankServiceImpl implements RankService{

    private final UserRepository userRepository;
    @Override
    public List<RankUserResponse> getTopRankings() {
        // 추천 수 기준 내림차순
        List<User> ranking = userRepository.findAll(Sort.by(Sort.Order.desc("recommendationCount")));

        return ranking.stream()
                .map(user -> new RankUserResponse(
                        (long) (ranking.indexOf(user) + 1),
                        user.getNickname(),
                        user.getRecommendationCount(),
                        user.getProfileImg()
                ))
                .limit(10)
                .collect(Collectors.toList());
    }
}
