package com.petmatz.domain.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignUpResponseDto extends LogInResponseDto {
    private Long id;

    private String imgURL;

    private SignUpResponseDto(Long id, String imgURL) {
        super();
        this.id=id;
        this.imgURL = imgURL;
    }

    // 1. 중복된 ID 응답
    public static ResponseEntity<LogInResponseDto> duplicateId() {
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    // 2. 인증 실패 응답
    public static ResponseEntity<LogInResponseDto> certificationFail() {
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    // 3. 역할이 잘못된 경우
    public static ResponseEntity<LogInResponseDto> wrongRole() {
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.WRONG_ROLE, ResponseMessage.WRONG_ROLE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    // 4. 회원가입 성공 응답
    public static ResponseEntity<SignUpResponseDto> success(Long id, String imgURL) {
        SignUpResponseDto responseBody = new SignUpResponseDto(id,imgURL);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    // 5. 위치 정보 확인 실패 응답
    public static ResponseEntity<LogInResponseDto> locationFail() {
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.LOCATION_FAIL, ResponseMessage.LOCATION_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    // 6. 비밀번호 정책 위반 응답
    public static ResponseEntity<LogInResponseDto> invalidPassword() {
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.INVALID_PASSWORD, ResponseMessage.INVALID_PASSWORD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    // 7. 필수 정보 누락 응답
    public static ResponseEntity<LogInResponseDto> missingRequiredFields() {
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.MISSING_FIELDS, ResponseMessage.MISSING_FIELDS);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    // 8. 알 수 없는 오류 발생
    public static ResponseEntity<LogInResponseDto> unknownError() {
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.UNKNOWN_ERROR, ResponseMessage.UNKNOWN_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
}