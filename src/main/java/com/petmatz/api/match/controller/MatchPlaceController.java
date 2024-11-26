//package com.petmatz.api.match.controller;
//
//import com.petmatz.domain.match.response.UserResponse;
//import com.petmatz.domain.match.service.MatchPlaceService;
//import com.petmatz.domain.match.service.TotalScoreService;
//import com.petmatz.domain.user.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/match")
//@RequiredArgsConstructor
//public class MatchPlaceController {
//
//    private final MatchPlaceService matchPlaceService;
//    private final TotalScoreService totalScoreService;
//
//    @PostMapping("/find-matches")
//    public ResponseEntity<Double> findMatches(@RequestBody User user) {
//        try {
//            List<UserResponse> allUsers = totalScoreService.getUsersWithinBoundingBox(user);
//
//            // 거리 계산 및 총점 계산
//            double totalScore = matchPlaceService.findMatchesWithinDistance(user, allUsers);
//
//            // 디버깅을 위한 출력
//            System.out.println("Total Score: " + totalScore);
//
//            return ResponseEntity.ok(totalScore);
//        } catch (Exception e) {
//            // 예외 처리
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0.0);
//        }
//    }
//}
//
