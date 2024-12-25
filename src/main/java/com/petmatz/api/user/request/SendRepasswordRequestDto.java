package com.petmatz.api.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendRepasswordRequestDto {
    private String accountId;
}
