package com.petmatz.api.match.controller;

import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.dto.response.UserResponse;
import com.petmatz.domain.match.service.TotalScoreService;
import com.petmatz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/total-score")
@RequiredArgsConstructor
public class TotalScoreTestController {

    private final TotalScoreService totalScoreService;

    @PostMapping("/calculate")
    public List<MatchScoreResponse> calculateTotalScore(@RequestBody Long userId) {
        return totalScoreService.calculateTotalScore(userId);
    }

    @PostMapping("/get-users")
    public List<UserResponse> getUsersWithinBoundingBox(@RequestBody Long userId) {
        return totalScoreService.getUsersWithinBoundingBox(userId);
    }
}

