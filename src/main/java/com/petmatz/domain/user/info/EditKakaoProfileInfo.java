package com.petmatz.domain.user.info;

import com.petmatz.domain.user.constant.Gender;
import com.petmatz.domain.user.constant.PreferredSize;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EditKakaoProfileInfo {
    private String profileImg;

    private String nickname;

    private List<PreferredSize> preferredSizes;

    private String introduction;

    private boolean isCareAvailable;

    private Gender gender;

    private String mbti;

    private Double latitude;

    private Double longitude;
}
