package com.petmatz.domain.user.response;

import com.petmatz.domain.user.constant.*;
import com.petmatz.domain.user.entity.User;
import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class SignInResponseDto extends LogInResponseDto {
    private Long id;
    private String accountId;
    private String nickname;
    private LoginRole loginRole;
    private LoginType loginType;
    private List<PreferredSize> preferredSizes; // 변경된 필드
    private Gender gender;
    private Boolean isRegistered;
    private Integer recommendationCount;
    private Integer careCompletionCount;
    private Boolean isCareAvailable;
    private String mbti;
    private String region;
    private Integer regionCode;

    private SignInResponseDto(User user) {
        super();
        this.id = user.getId();
        this.accountId = user.getAccountId();
        this.nickname = user.getNickname();
        this.loginRole = user.getLoginRole();
        this.loginType = user.getLoginType();
        this.preferredSizes = user.getPreferredSizes(); // 리스트를 직접 할당
        this.gender = user.getGender();
        this.isRegistered = user.getIsRegistered();
        this.recommendationCount = user.getRecommendationCount();
        this.careCompletionCount = user.getCareCompletionCount();
        this.isCareAvailable = user.getCareAvailable();
        this.mbti = user.getMbti();
        this.region = user.getRegion();
        this.regionCode=user.getRegionCode();
    }

    public static ResponseEntity<SignInResponseDto> success(User user) {
        SignInResponseDto responseBody = new SignInResponseDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> signInFail() {
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}