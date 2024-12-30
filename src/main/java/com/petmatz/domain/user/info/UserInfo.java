package com.petmatz.domain.user.info;

import lombok.Builder;

@Builder
public record UserInfo(

        Long id,
        String nickname,
        String email,
        String profileImg

) {
}
