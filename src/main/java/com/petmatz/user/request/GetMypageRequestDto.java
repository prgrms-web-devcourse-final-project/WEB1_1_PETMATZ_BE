package com.petmatz.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class GetMypageRequestDto {
    @NotNull
    private String accountId;
}
