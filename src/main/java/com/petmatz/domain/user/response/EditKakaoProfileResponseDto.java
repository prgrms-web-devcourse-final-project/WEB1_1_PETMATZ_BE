package com.petmatz.domain.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class EditKakaoProfileResponseDto extends LogInResponseDto {
    private EditKakaoProfileResponseDto(){
        super();
    }

    public static ResponseEntity<LogInResponseDto> idNotFound(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.ID_NOT_FOUND, ResponseMessage.ID_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> editFailed(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.EDIT_FAIL, ResponseMessage.EDIT_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
