package com.petmatz.domain.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class EditMyProfileResponseDto extends LogInResponseDto {
    private EditMyProfileResponseDto(){
        super();
    }

    public static ResponseEntity<LogInResponseDto> editFailed(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.EDIT_FAIL, ResponseMessage.EDIT_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
