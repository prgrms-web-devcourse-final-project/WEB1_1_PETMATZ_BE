package com.petmatz.api.user.request;

import com.petmatz.domain.user.constant.PreferredSize;
import com.petmatz.domain.user.info.EditMyProfileInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EditMyProfileRequestDto {
    private String nickname;

    private List<PreferredSize> preferredSizes;

    private String introduction;

    private boolean careAvailable;

    private String profileImg;

    public static EditMyProfileInfo of(EditMyProfileRequestDto reqDto) {
        return EditMyProfileInfo.builder()
                .nickname(reqDto.getNickname())
                .preferredSizes(reqDto.getPreferredSizes())
                .introduction(reqDto.getIntroduction())
                .careAvailable(reqDto.isCareAvailable()
                )
                .profileImg(reqDto.getProfileImg())
                .build();
    }
}
