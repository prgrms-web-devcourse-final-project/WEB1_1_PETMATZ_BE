package com.petmatz.domain.user.info;

import com.petmatz.domain.user.constant.Gender;
import com.petmatz.domain.user.constant.PreferredSize;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SignUpInfo {
    private String accountId;

    private String password;

    private String certificationNumber;

    private String nickname;

    private String profileImg;

    private Gender gender;

    private List<PreferredSize> preferredSizes;

    private Boolean isCareAvailable;

    private String introduction;

    private String mbti;

    private Double latitude;

    private Double longitude;
}
