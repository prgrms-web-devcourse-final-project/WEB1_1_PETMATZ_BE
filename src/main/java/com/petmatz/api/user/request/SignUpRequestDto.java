package com.petmatz.api.user.request;

import com.petmatz.domain.user.constant.Gender;
import com.petmatz.domain.user.constant.PreferredSize;
import com.petmatz.domain.user.info.SignUpInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {
    @NotBlank
    private String accountId; // account_id와 매핑

    @NotBlank
    private String password;

    @NotBlank
    private String certificationNumber;

    @NotBlank
    private String nickname;

    private String profileImg;

    @NotBlank
    private Gender gender; // 'Male' 또는 'Female'

    @NotBlank
    private List<PreferredSize> preferredSizes; // 여러 값을 허용하도록 변경

    @NotBlank
    private Boolean isCareAvailable;

    private String introduction; //선택이므로 @NotBlank 제외

    @NotBlank
    private String mbti;

    private Double latitude;

    private Double longitude;

    public static SignUpInfo of(SignUpRequestDto reqDto) {
        return SignUpInfo.builder()
                .accountId(reqDto.getAccountId())
                .password(reqDto.getPassword())
                .certificationNumber(reqDto.getCertificationNumber())
                .nickname(reqDto.getNickname())
                .profileImg(reqDto.getProfileImg())
                .gender(reqDto.getGender())
                .preferredSizes(reqDto.getPreferredSizes())
                .isCareAvailable(reqDto.getIsCareAvailable())
                .introduction(reqDto.getIntroduction())
                .mbti(reqDto.getMbti())
                .latitude(reqDto.getLatitude())
                .longitude(reqDto.getLongitude())
                .build();
    }
}