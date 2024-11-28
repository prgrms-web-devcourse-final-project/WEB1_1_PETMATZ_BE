package com.petmatz.api.match.controller;

import com.petmatz.domain.match.dto.response.DetailedMatchResultResponse;
import com.petmatz.domain.match.dto.response.MatchResultResponse;
import com.petmatz.domain.match.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping // 추후에 userId contextHolder 로 변경
    public ResponseEntity<List<DetailedMatchResultResponse>> getPageDetails(@RequestParam Long userId,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "5") int size) {

        List<DetailedMatchResultResponse> userDetails = matchService.getPageUserDetailsFromRedis(userId, page, size);

        if (userDetails.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // 결과 반환
        return ResponseEntity.ok(userDetails);
    }

}


