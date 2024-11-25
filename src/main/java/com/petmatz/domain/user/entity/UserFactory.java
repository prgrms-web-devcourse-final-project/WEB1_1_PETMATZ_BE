package com.petmatz.domain.user.entity;


import com.petmatz.user.request.info.EditMyProfileInfo;
import com.petmatz.user.request.info.SignUpInfo;

import java.time.LocalDateTime;

public class UserFactory {

    public static User createNewUser(SignUpInfo info, String encodedPassword) {
        return User.builder()
                .accountId(info.getAccountId())
                .password(encodedPassword)
                .nickname(info.getNickname())
                .loginRole(User.LoginRole.ROLE_USER)
                .gender(info.getGender())
                .preferredSize(info.getPreferredSize())
                .introduction(info.getIntroduction())
                .isCareAvailable(info.getIsCareAvailable())
                .role(User.Role.Dol)
                .loginType(User.LoginType.Normal)
                .isRegistered(false)
                .recommendationCount(0)
                .careCompletionCount(0)
                .isDeleted(false)
                .timeWage(info.getTimeWage())
                .monthWage(info.getMonthWage())
                .mbti(info.getMbti())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User createDeletedUser(User user) {
        return User.builder()
                .id(user.getId())
                .accountId(user.getAccountId() + "-deleted")
                .password("deleted-password")
                .email(user.getEmail() + "-deleted")
                .nickname(null)
                .profileImg(null)
                .loginRole(null)
                .role(null)
                .loginType(null)
                .gender(null)
                .preferredSize(user.getPreferredSize())
                .introduction(null)
                .isCareAvailable(user.getIsCareAvailable())
                .isDeleted(true)
                .timeWage(user.getTimeWage())
                .monthWage(user.getMonthWage())
                .mbti(user.getMbti())
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    public static User createUpdatedUser(User user, EditMyProfileInfo info) {
        return User.builder()
                .id(user.getId())
                .accountId(user.getAccountId())
                .password(user.getPassword())
                .email(user.getEmail())
                .nickname(info.getNickname()) // 닉네임 업데이트
                .profileImg(user.getProfileImg())
                .loginRole(user.getLoginRole())
                .role(user.getRole())
                .loginType(user.getLoginType())
                .gender(user.getGender())
                .preferredSize(info.getPreferredSize()) // 선호 크기 업데이트
                .introduction(info.getIntroduction()) // 자기소개 업데이트
                .isCareAvailable(info.isCareAvailable()) // 돌봄 가능 여부 업데이트
                .isRegistered(user.getIsRegistered())
                .recommendationCount(user.getRecommendationCount())
                .careCompletionCount(user.getCareCompletionCount())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .isDeleted(user.getIsDeleted()) // 기존 삭제 여부 유지
                .timeWage(info.getTimeWage()) // 시간당 임금 업데이트
                .monthWage(info.getMonthWage()) // 월 임금 업데이트
                .mbti(user.getMbti())
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now()) // 수정 시간 갱신
                .build();
    }


    public static User createUpdatedPasswordUser(User user, String encodedRePasswordNum) {
        return User.builder()
                .id(user.getId())
                .accountId(user.getAccountId())
                .password(encodedRePasswordNum) // 임시 비밀번호로 업데이트
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .loginRole(user.getLoginRole())
                .role(user.getRole())
                .loginType(user.getLoginType())
                .gender(user.getGender())
                .preferredSize(user.getPreferredSize())
                .introduction(user.getIntroduction())
                .isCareAvailable(user.getIsCareAvailable())
                .isRegistered(user.getIsRegistered())
                .recommendationCount(user.getRecommendationCount())
                .careCompletionCount(user.getCareCompletionCount())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .isDeleted(user.getIsDeleted()) // 기존 삭제 여부 유지
                .timeWage(user.getTimeWage())
                .monthWage(user.getMonthWage())
                .mbti(user.getMbti())
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now()) // 수정 시간 갱신
                .build();
    }
}