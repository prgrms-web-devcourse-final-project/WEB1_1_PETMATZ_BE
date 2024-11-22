package com.petmatz.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignUpResponseDto extends LogInResponseDto {


    private SignUpResponseDto(String role) {
        super();
    }

    public static ResponseEntity<LogInResponseDto> duplicateId(){
        LogInResponseDto responseBody=new LogInResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> certificationFail(){
        LogInResponseDto responseBody=new LogInResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> wrongRole() {
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.WRONG_ROLE, ResponseMessage.WRONG_ROLE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
