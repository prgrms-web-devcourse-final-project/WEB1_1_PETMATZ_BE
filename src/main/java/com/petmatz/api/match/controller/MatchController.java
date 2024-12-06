package com.petmatz.api.match.controller;

import com.petmatz.api.match.request.PenaltyScore;
import com.petmatz.domain.match.dto.response.PaginatedMatchResponse;
import com.petmatz.domain.match.service.MatchScoreService;
import com.petmatz.domain.match.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final MatchScoreService matchScoreService;


    // TODO 추후에 userId 변경 (JWT)
    @GetMapping("/showmetz")
    public ResponseEntity<PaginatedMatchResponse> getPageDetails(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        matchScoreService.calculateTotalScore(userId);
        PaginatedMatchResponse userDetails = matchService.getPageUserDetailsFromRedis(userId, page, size);

        if (userDetails.matchResults().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/matchfail")
    // Dto 구조 개선 예정
    public ResponseEntity<Void> matchFail(@RequestBody PenaltyScore penaltyScore) {
        matchScoreService.decreaseScore(penaltyScore.userId(), penaltyScore.targetId());
        return ResponseEntity.ok().build();
    }
}


