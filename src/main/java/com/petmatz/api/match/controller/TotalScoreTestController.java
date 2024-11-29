package com.petmatz.api.match.controller;

import com.petmatz.api.match.request.UserIdRequest;
import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.dto.response.UserResponse;
import com.petmatz.domain.match.service.MatchScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/total-score")
@RequiredArgsConstructor
public class TotalScoreTestController {

    private final MatchScoreService matchScoreService;

    @PostMapping("/calculate")
    public List<MatchScoreResponse> calculateTotalScore(@RequestBody UserIdRequest userId) {
        return matchScoreService.calculateTotalScore(userId.userId());
    }

    @PostMapping("/get-users")
    public List<UserResponse> getUsersWithinBoundingBox(@RequestBody UserIdRequest userId) {
        return matchScoreService.getUsersWithinBoundingBox(userId.userId());
    }
}

