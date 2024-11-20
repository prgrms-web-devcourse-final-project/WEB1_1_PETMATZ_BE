package com.petmatz.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import com.petmatz.user.entity.UserEntity;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignInResponseDto extends LogInResponseDto {
    private String token;
    private int expirationTime;
    private UserEntity.LoginRole loginRole;

    private SignInResponseDto(String token, UserEntity.LoginRole loginRole) {
        super();
        this.token = token;
        this.expirationTime=3600; // 1 hour
        this.loginRole = loginRole;
    }

    public static ResponseEntity<SignInResponseDto> success(String token,UserEntity.LoginRole loginRole) {
        SignInResponseDto responseBody = new SignInResponseDto(token,loginRole);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> signInFail(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
