package com.petmatz.user.request;

import com.petmatz.user.entity.User;
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
    private String certificationNumber;

    @NotBlank
    private String nickname;

    @NotBlank
    private User.Gender gender; // 'Male' 또는 'Female'

    @NotBlank
    private User.PreferredSize preferredSize; // 여러 값을 허용하도록 변경

    @NotBlank
    private Boolean isCareAvailable;

    @NotBlank
    private Integer timeWage;

    @NotBlank
    private Integer monthWage;

    private String introduction; //선택이므로 @NotBlank 제외
}