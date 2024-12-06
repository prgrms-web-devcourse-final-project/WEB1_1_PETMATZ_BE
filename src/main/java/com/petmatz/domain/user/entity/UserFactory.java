package com.petmatz.domain.user.entity;


import com.petmatz.domain.user.constant.LoginRole;
import com.petmatz.domain.user.constant.LoginType;
import com.petmatz.domain.user.info.SignUpInfo;

import java.time.LocalDateTime;

public class UserFactory {

    public static User createNewUser(SignUpInfo info, String encodedPassword, String region, Integer regionCode, String imgURL) {

        return User.builder()
                .accountId(info.getAccountId())
                .password(encodedPassword)
                .nickname(info.getNickname())
                .profileImg(imgURL)
                .loginRole(LoginRole.ROLE_USER)
                .loginType(LoginType.NORMAL)
                .gender(info.getGender())
                .preferredSizes(info.getPreferredSizes())
                .introduction(info.getIntroduction())
                .isCareAvailable(info.getIsCareAvailable())
                .isRegistered(false)
                .recommendationCount(0)
                .careCompletionCount(0)
                .mbti(info.getMbti())
                .latitude(info.getLatitude())
                .longitude(info.getLongitude())
                .region(region)
                .regionCode(regionCode)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Heart createHeart(Long myId, Long heartedId) {
        return Heart.builder()
                .myId(myId)
                .heartedId(heartedId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}