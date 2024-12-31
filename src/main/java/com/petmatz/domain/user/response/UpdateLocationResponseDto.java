package com.petmatz.domain.user.response;

import com.petmatz.domain.user.entity.User;
import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class UpdateLocationResponseDto extends LogInResponseDto{
    private String region;
    private Integer regionCode;

    private UpdateLocationResponseDto(String region,Integer regionCode){
        super();
        this.region = region;
        this.regionCode=regionCode;
    }

    public static ResponseEntity<LogInResponseDto> wrongLocation(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.WRONG_LOCATION, ResponseMessage.WRONG_LOCATION);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }

    public static ResponseEntity<UpdateLocationResponseDto> success(String region,Integer regionCode) { // 반환 타입 수정
        UpdateLocationResponseDto responseBody = new UpdateLocationResponseDto(region,regionCode);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
