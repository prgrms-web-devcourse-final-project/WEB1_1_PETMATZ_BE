package com.petmatz.domain.user.service;

import com.petmatz.domain.user.response.RankUserResponse;

import java.util.List;

public interface RankService {
    List<RankUserResponse> getTopRankings();

    List<RankUserResponse> getTopRankingsByRegion();

}
