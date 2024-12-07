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


    @GetMapping("/showmetz")
    public ResponseEntity<PaginatedMatchResponse> getPageDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        matchScoreService.calculateTotalScore();
        PaginatedMatchResponse userDetails = matchService.getPageUserDetailsFromRedis(page, size);

        if (userDetails.matchResults().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/matchfail")
    public ResponseEntity<Void> matchFail(@RequestBody PenaltyScore penaltyScore) {
        matchScoreService.decreaseScore(penaltyScore.targetId());
        return ResponseEntity.ok().build();
    }
}


