package com.petmatz.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {
    @NotBlank
    private String accountId; // account_id와 매핑

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$")
    private String password;

    @NotBlank
    private String certificationNumber; // 기존 필드 유지

    @NotBlank
    private String nickname;

    @NotBlank
    private String gender; // 'Male' 또는 'Female'

    @NotBlank
    private String preferredSize; // 'Small', 'Medium', 'Large'

    @NotBlank
    private String introduction;

    @NotBlank
    private Boolean isCareAvailable = false;
}