package com.petmatz.user.request;

import com.petmatz.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditMyProfileRequestDto {
    private String nickname;

    private User.PreferredSize preferredSize;

    private String introduction;

    private boolean isCareAvailable;

    private Integer timeWage;

    private Integer monthWage;
}
