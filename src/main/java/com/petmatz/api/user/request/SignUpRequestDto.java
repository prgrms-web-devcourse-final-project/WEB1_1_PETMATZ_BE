package com.petmatz.api.user.request;

import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.info.SignUpInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,20}$")
    private String password;

    @NotBlank
    private String certificationNumber;

    @NotBlank
    private String nickname;

    @NotBlank
    private User.Gender gender; // 'Male' 또는 'Female'

    @NotBlank
    private String preferredSize; // 여러 값을 허용하도록 변경

    @NotBlank
    private Boolean isCareAvailable;

    private String introduction; //선택이므로 @NotBlank 제외

    @NotBlank
    private String mbti;

    public static SignUpInfo of(SignUpRequestDto reqDto) {
        return SignUpInfo.builder()
                .accountId(reqDto.getAccountId())
                .password(reqDto.getPassword())
                .certificationNumber(reqDto.getCertificationNumber())
                .nickname(reqDto.getNickname())
                .gender(reqDto.getGender())
                .preferredSize(reqDto.getPreferredSize())
                .isCareAvailable(reqDto.getIsCareAvailable())
                .introduction(reqDto.getIntroduction())
                .mbti(reqDto.getMbti())
                .build();
    }
}