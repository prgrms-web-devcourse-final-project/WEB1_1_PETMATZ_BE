package com.petmatz.api.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailCertificationRequestDto {
    @Email
    @NotBlank
    private String accountId;
}
