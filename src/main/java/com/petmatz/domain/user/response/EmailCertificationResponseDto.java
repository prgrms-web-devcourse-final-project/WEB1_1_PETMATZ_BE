package com.petmatz.domain.user.response;


import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class EmailCertificationResponseDto extends LogInResponseDto {

    private EmailCertificationResponseDto(){
        super();
    }


    public static ResponseEntity<LogInResponseDto> duplicateId(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> mailSendFail(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.MAIL_FAIL, ResponseMessage.MAIL_FAIL);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> alreadyDeletedUser() {
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.DELETED_USER, ResponseMessage.DELETED_USER);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }
}
