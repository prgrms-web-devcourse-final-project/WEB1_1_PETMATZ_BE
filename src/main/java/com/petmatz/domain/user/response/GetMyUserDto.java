package com.petmatz.domain.user.response;

import com.petmatz.domain.user.constant.Gender;
import com.petmatz.domain.user.constant.PreferredSize;
import com.petmatz.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor @AllArgsConstructor
public class GetMyUserDto {
    private Long id;
    private String accountId;
    private String nickname;
    private String profileImg;
    private List<PreferredSize> preferredSizes; // 변경: List로 수정
    private Gender gender;
    private String introduction;
    private Boolean isRegistered;
    private Integer recommendationCount;
    private Integer careCompletionCount;
    private Boolean isCareAvailable;
    private Double latitude;
    private Double longitude;
    private String mbti;
    private String region;

    public GetMyUserDto(User user) {
        this.id = user.getId();
        this.accountId = user.getAccountId();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
        this.preferredSizes = user.getPreferredSizes(); // 수정: 리스트 그대로 할당
        this.gender = user.getGender();
        this.introduction=user.getIntroduction();
        this.isRegistered = user.getIsRegistered();
        this.recommendationCount = user.getRecommendationCount();
        this.careCompletionCount = user.getCareCompletionCount();
        this.isCareAvailable = user.getCareAvailable();
        this.latitude = user.getLatitude();
        this.longitude = user.getLongitude();
        this.mbti = user.getMbti();
        this.region = user.getRegion();
    }
}
