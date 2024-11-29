package com.petmatz.user.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class LogInResponseDto {
    private String responseCode;
    private String message;

    public LogInResponseDto() {
        this.responseCode = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
    }

    public static ResponseEntity<LogInResponseDto> success() {
        LogInResponseDto responseBody = new LogInResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> databaseError(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(500).body(responseBody); //500 Error == Internal Server Error
    }

    public static ResponseEntity<LogInResponseDto> validationFail(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL);
        return ResponseEntity.status(400).body(responseBody); //400 Error == Bad Request
    }

}
