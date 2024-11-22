package com.petmatz.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class SendRepasswordRequestDto {
    private String accountId;
}
