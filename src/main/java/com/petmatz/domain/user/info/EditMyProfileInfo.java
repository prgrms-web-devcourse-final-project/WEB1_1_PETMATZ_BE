package com.petmatz.domain.user.info;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditMyProfileInfo {
    private String nickname;

    private String preferredSize;

    private String introduction;

    private boolean isCareAvailable;

    private Integer timeWage;

    private Integer monthWage;
}
