package com.petmatz.user.response;

import com.petmatz.domain.user.entity.User;
import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Collectors;

@Getter
public class SignInResponseDto extends LogInResponseDto {
    private Long id;
    private String accountId;
    private String nickname;
    private User.LoginRole loginRole;
    private User.LoginType loginType;
    private User.Role role;
    private String preferredSize;
    private User.Gender gender;
    private Boolean isRegistered;
    private Integer recommendationCount;
    private Integer careCompletionCount;
    private Integer timeWage;
    private Integer monthWage;
    private Boolean isCareAvailable;

    private SignInResponseDto(User user) {
        super();
        this.id = user.getId();
        this.accountId = user.getAccountId();
        this.nickname = user.getNickname();
        this.loginRole = user.getLoginRole();
        this.loginType = user.getLoginType();
        this.role = user.getRole();
        this.preferredSize =user.getPreferredSize();
        this.gender = user.getGender();
        this.isRegistered = user.getIsRegistered();
        this.recommendationCount = user.getRecommendationCount();
        this.careCompletionCount = user.getCareCompletionCount();
        this.timeWage = user.getTimeWage();
        this.monthWage = user.getMonthWage();
        this.isCareAvailable = user.getIsCareAvailable();
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