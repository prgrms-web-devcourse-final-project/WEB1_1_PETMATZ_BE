package com.petmatz.user.request.info;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInInfo {
    private String accountId;
    private String password;
}
