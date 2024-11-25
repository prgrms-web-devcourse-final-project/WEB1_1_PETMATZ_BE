package com.petmatz.user.request;

import com.petmatz.user.request.info.SignInInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequestDto {
    @Email
    @NotBlank
    private String accountId;

    @NotBlank
    private String password;

    public static SignInInfo of(SignInRequestDto reqDto) {
        return SignInInfo.builder()
                .accountId(reqDto.getAccountId())
                .password(reqDto.getPassword())
                .build();
    }
}
