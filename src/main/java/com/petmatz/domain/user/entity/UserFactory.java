package com.petmatz.domain.user.entity;


import com.petmatz.domain.user.constant.LoginRole;
import com.petmatz.domain.user.constant.LoginType;
import com.petmatz.domain.user.constant.Role;
import com.petmatz.domain.user.info.EditMyProfileInfo;
import com.petmatz.domain.user.info.SignUpInfo;
import com.petmatz.domain.user.info.UpdateLocationInfo;

import java.time.LocalDateTime;

public class UserFactory {

    public static User createNewUser(SignUpInfo info, String encodedPassword, String region) {

        return User.builder()
                .accountId(info.getAccountId())
                .password(encodedPassword)
                .nickname(info.getNickname())
                .email(info.getAccountId())
                .profileImg(null)
                .loginRole(LoginRole.ROLE_USER)
                .loginType(LoginType.NORMAL)
                .role(Role.DOL)
                .gender(info.getGender())
                .preferredSizes(info.getPreferredSizes())
                .introduction(info.getIntroduction())
                .isCareAvailable(info.getIsCareAvailable())
                .isRegistered(false)
                .recommendationCount(0)
                .careCompletionCount(0)
                .isDeleted(false)
                .mbti(info.getMbti())
                .latitude(info.getLatitude())
                .longitude(info.getLongitude())
                .region(region)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User createDeletedUser(User user) {
        return User.builder()
                .id(user.getId())
                .accountId(user.getAccountId() + "-deleted")
                .password("deleted-password")
                .nickname(user.getNickname()+"-deleted")
                .email(user.getEmail() + "-deleted")
                .profileImg(null)
                .loginRole(null)
                .loginType(null)
                .role(null)
                .gender(null)
                .preferredSizes(user.getPreferredSizes())
                .introduction(null)
                .isCareAvailable(user.getIsCareAvailable())
                .isRegistered(true)
                .recommendationCount(0)
                .careCompletionCount(0)
                .latitude(null)
                .longitude(null)
                .isDeleted(true)
                .mbti("deleted")
                .region(null)
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    public static User createUpdatedUser(User user, EditMyProfileInfo info) {
        return User.builder()
                .id(user.getId())
                .accountId(user.getAccountId())
                .password(user.getPassword())
                .nickname(info.getNickname())
                .email(user.getEmail())
                .profileImg(info.getProfileImg())
                .loginRole(user.getLoginRole())
                .loginType(user.getLoginType())
                .role(user.getRole())
                .gender(user.getGender())
                .preferredSizes(info.getPreferredSizes())
                .introduction(info.getIntroduction())
                .isCareAvailable(info.isCareAvailable())
                .isRegistered(user.getIsRegistered())
                .recommendationCount(user.getRecommendationCount())
                .careCompletionCount(user.getCareCompletionCount())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .isDeleted(user.getIsDeleted())
                .mbti(user.getMbti())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .region(user.getRegion())
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    public static User createUpdatedPasswordUser(User user, String encodedRePasswordNum) {
        return User.builder()
                .id(user.getId())
                .accountId(user.getAccountId())
                .password(encodedRePasswordNum)
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .loginRole(user.getLoginRole())
                .loginType(user.getLoginType())
                .role(user.getRole())
                .gender(user.getGender())
                .preferredSizes(user.getPreferredSizes())
                .introduction(user.getIntroduction())
                .isCareAvailable(user.getIsCareAvailable())
                .isRegistered(user.getIsRegistered())
                .recommendationCount(user.getRecommendationCount())
                .careCompletionCount(user.getCareCompletionCount())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .isDeleted(user.getIsDeleted())
                .mbti(user.getMbti())
                .region(user.getRegion())
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User createLocationUpdateUser(User user, UpdateLocationInfo info, String region) {
        return User.builder()
                .id(user.getId())
                .accountId(user.getAccountId())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .loginRole(user.getLoginRole())
                .loginType(user.getLoginType())
                .role(user.getRole())
                .gender(user.getGender())
                .preferredSizes(user.getPreferredSizes())
                .introduction(user.getIntroduction())
                .isCareAvailable(user.getIsCareAvailable())
                .isRegistered(user.getIsRegistered())
                .recommendationCount(user.getRecommendationCount())
                .careCompletionCount(user.getCareCompletionCount())
                .latitude(info.getLatitude())
                .longitude(info.getLongitude())
                .isDeleted(user.getIsDeleted())
                .mbti(user.getMbti())
                .region(region)
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    public static User createRecommendationUpdateUser(User user, Integer recommendationCount) {
        return User.builder()
                .id(user.getId())
                .accountId(user.getAccountId())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .loginRole(user.getLoginRole())
                .loginType(user.getLoginType())
                .role(user.getRole())
                .gender(user.getGender())
                .preferredSizes(user.getPreferredSizes())
                .introduction(user.getIntroduction())
                .isCareAvailable(user.getIsCareAvailable())
                .isRegistered(user.getIsRegistered())
                .recommendationCount(recommendationCount)
                .careCompletionCount(user.getCareCompletionCount())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .isDeleted(user.getIsDeleted())
                .mbti(user.getMbti())
                .region(user.getRegion())
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

    }
}