package com.petmatz.domain.user.info;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepasswordInfo {
    private String currentPassword;
    private String newPassword;
}
