package com.petmatz.user.request;

import com.petmatz.user.request.info.RepasswordInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepasswordRequestDto {
    private String currentPassword;
    private String newPassword;

    public static RepasswordInfo of(RepasswordRequestDto reqDto) {
        return RepasswordInfo.builder()
                .currentPassword(reqDto.getCurrentPassword())
                .newPassword(reqDto.getNewPassword())
                .build();
    }
}
