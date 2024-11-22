package com.petmatz.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckCertificationRequestDto {
    @Email
    @NotBlank
    private String accountId;

    @NotBlank
    private String certificationNumber;
}
