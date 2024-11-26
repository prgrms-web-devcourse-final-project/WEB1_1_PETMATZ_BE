//package com.petmatz.match;
//
//import com.petmatz.domain.match.response.UserMatchResponse;
//import com.petmatz.domain.match.service.MatchPlaceService;
//import org.junit.jupiter.api.Test;
//import com.petmatz.domain.user.entity.User;
//
//import java.util.Arrays;
//import java.util.List;
//
//
//public class MatchResultPlaceServiceTest {
//
//    private final MatchPlaceService matchPlaceService = new MatchPlaceService();
//
//    @Test
//    public void testFindMatchesWithinDistance() {
//        // 기준 사용자 (서울)
//        User currentUser = User.builder()
//                .id(1L)
//                .latitude("37.5665")
//                .longitude("126.9780")
//                .build();
//
//        // 다른 사용자들
//        User user2 =  User.builder()
//                .id(2L)
//                .latitude("37.5670")
//                .longitude("126.9785")
//                .build();
//
//        User user3 = User.builder()
//                .id(3L)
//                .latitude("37.5750")
//                .longitude("126.9850")
//                .build();
//
//        User user4 = User.builder()
//                .id(4L)
//                .latitude("37.5850")
//                .longitude("126.9950")
//                .build();
//
//        User user5 = User.builder()
//                .id(5L)
//                .latitude("37.6000")
//                .longitude("126.9990")
//                .build();
//
//        List<User> allUsers = Arrays.asList(currentUser, user2, user3, user4, user5);
//
//        List<UserMatchResponse> matches = matchPlaceService.findMatchesWithinDistance(currentUser, allUsers);
//
//        // 결과 출력
//        matches.forEach(match -> {
//            System.out.println("User ID: " + match.userId());
//            System.out.println("Distance: " + match.distance() + " km");
//            System.out.println("Region Score: " + match.totalScore());
//            System.out.println("----------------");
//        });
//
//        assert matches.size() == 4; // 4명의 대상 (자기 자신 제외)
//        assert matches.get(0).totalScore() == 40.0; // User2: 0.07km, 40점
//        assert matches.get(1).totalScore() == 35.0; // User3: 1.13km, 35점
//        assert matches.get(2).totalScore() == 25.0; // User4: 2.54km, 25점
//        assert matches.get(3).totalScore() == 20.0;  // User5: 4.16km, 20점
//
//    }
//
//}
