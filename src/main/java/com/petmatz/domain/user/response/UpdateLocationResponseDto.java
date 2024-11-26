package com.petmatz.domain.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UpdateLocationResponseDto extends LogInResponseDto{

    private UpdateLocationResponseDto(){
        super();
    }

    public static ResponseEntity<LogInResponseDto> wrongLocation(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.WRONG_LOCATION, ResponseMessage.WRONG_LOCATION);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }
}
