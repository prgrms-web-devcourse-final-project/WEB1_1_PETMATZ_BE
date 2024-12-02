package com.petmatz.domain.user.service;

import com.petmatz.domain.user.response.RankUserResponse;

import java.util.List;

public interface RankService {
    List<RankUserResponse> getTopRankings();

    List<RankUserResponse> getTopRankingsByRegion(Long userId); // 추후에는 매개 변수 없앨 예정 임시 테스트

}
