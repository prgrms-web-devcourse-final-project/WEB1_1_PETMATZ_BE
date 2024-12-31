package com.petmatz.domain.user.info;

import jakarta.persistence.Column;
import lombok.Builder;

@Builder
public record UserInfo(

        Long id,
        String nickname,
        String email,
        String profileImg

) {
}
