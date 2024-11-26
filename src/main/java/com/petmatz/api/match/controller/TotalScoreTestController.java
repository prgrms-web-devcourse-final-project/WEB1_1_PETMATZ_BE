package com.petmatz.api.match.controller;

import com.petmatz.domain.match.response.MatchResultResponse;
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
    public List<MatchResultResponse> calculateTotalScore(@RequestBody User user, @RequestParam double radiusKm) {
        return totalScoreService.calculateTotalScore(user, radiusKm);
    }

    @GetMapping("/get-users")
    public List<User> getUsersWithinBoundingBox(@RequestBody User user, @RequestParam double radiusKm) {
        return totalScoreService.getUsersWithinBoundingBox(user, radiusKm);
    }
}

