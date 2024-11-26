package com.petmatz.domain.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class HeartingResponseDto extends LogInResponseDto {

    private HeartingResponseDto(){
        super();
    }

    public static ResponseEntity<LogInResponseDto> heartedIdNotFound(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.HEARTED_ID_NOT_FOUND, ResponseMessage.HEARTED_ID_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
