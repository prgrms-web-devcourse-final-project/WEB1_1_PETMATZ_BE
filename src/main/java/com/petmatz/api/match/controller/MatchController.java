package com.petmatz.api.match.controller;

import com.petmatz.domain.match.dto.response.DetailedMatchResultResponse;
import com.petmatz.domain.match.dto.response.MatchResultResponse;
import com.petmatz.domain.match.service.MatchService;
import com.petmatz.domain.match.service.TotalScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final TotalScoreService totalScoreService;

    // TODO 추후에 userId 변경 (JWT)
    @GetMapping("/showmetz")
    public ResponseEntity<List<DetailedMatchResultResponse>> getPageDetails
            (@RequestParam Long userId,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "5") int size) {
        totalScoreService.calculateTotalScore(userId);
        List<DetailedMatchResultResponse> userDetails = matchService.getPageUserDetailsFromRedis(userId, page, size);

        if (userDetails.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userDetails);
    }

//    @PostMapping("/matchfail")
//    public ResponseEntity<?> matchFail () {
//    }
}


