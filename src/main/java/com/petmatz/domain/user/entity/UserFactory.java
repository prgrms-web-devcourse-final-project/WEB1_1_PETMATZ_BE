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
                .profileImg(info.getProfileImg())
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

}