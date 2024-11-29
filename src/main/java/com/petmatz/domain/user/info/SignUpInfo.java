package com.petmatz.domain.user.info;

import com.petmatz.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpInfo {
    private String accountId;

    private String password;

    private String certificationNumber;

    private String nickname;

    private User.Gender gender;

    private String preferredSize;

    private Boolean isCareAvailable;

    private String introduction;

    private String mbti;
}
