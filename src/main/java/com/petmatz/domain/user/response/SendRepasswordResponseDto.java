package com.petmatz.domain.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SendRepasswordResponseDto extends LogInResponseDto{

    private SendRepasswordResponseDto(){
        super();
    }

    public static ResponseEntity<LogInResponseDto> mailSendFail(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.MAIL_FAIL, ResponseMessage.MAIL_FAIL);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
