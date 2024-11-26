package com.petmatz.api.user.request;

import com.petmatz.domain.user.info.EditMyProfileInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditMyProfileRequestDto {
    private String nickname;

    private String preferredSize;

    private String introduction;

    private boolean isCareAvailable;

    private Integer timeWage;

    private Integer monthWage;

    public static EditMyProfileInfo of(EditMyProfileRequestDto reqDto) {
        return EditMyProfileInfo.builder()
                .nickname(reqDto.getNickname())
                .preferredSize(reqDto.getPreferredSize())
                .introduction(reqDto.getIntroduction())
                .isCareAvailable(reqDto.isCareAvailable())
                .timeWage(reqDto.getTimeWage())
                .monthWage(reqDto.getMonthWage())
                .build();
    }
}
