package com.petmatz.user.request;

import com.petmatz.user.request.info.CheckCertificationInfo;
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

    public static CheckCertificationInfo of(CheckCertificationRequestDto reqDto) {
        return CheckCertificationInfo.builder()
                .accountId(reqDto.getAccountId())
                .certificationNumber(reqDto.getCertificationNumber())
                .build();
    }
}
