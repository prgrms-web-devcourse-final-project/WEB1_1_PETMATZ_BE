package com.petmatz.domain.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class RepasswordResponseDto extends LogInResponseDto{
    private RepasswordResponseDto(){
        super();
    }

    public static ResponseEntity<LogInResponseDto> wrongPassword(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.WRONG_PASSWORD, ResponseMessage.WRONG_PASSWORD);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }
}
