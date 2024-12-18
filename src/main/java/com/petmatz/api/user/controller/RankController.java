package com.petmatz.api.user.controller;

import co.elastic.clients.elasticsearch._types.Rank;
import com.petmatz.api.global.dto.Response;
import com.petmatz.domain.user.response.RankUserResponse;
import com.petmatz.domain.user.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;

    @GetMapping("/api/top-rankings")
    public Response<List<RankUserResponse>> getTopRankings() {
        List<RankUserResponse> topRankings = rankService.getTopRankingsByRegion();
        return Response.success(topRankings);
    }
}

