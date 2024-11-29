package com.petmatz.domain.user.info;

import com.petmatz.domain.user.constant.PreferredSize;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EditMyProfileInfo {
    private String profileImg;

    private String nickname;

    private List<PreferredSize> preferredSizes;

    private String introduction;

    private boolean isCareAvailable;
}
