package com.petmatz.domain.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import com.petmatz.domain.user.entity.User;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetMyProfileResponseDto extends LogInResponseDto {

    private User user;

    public GetMyProfileResponseDto(User user) {
        super();
        this.user = user;
    }

    public static ResponseEntity<LogInResponseDto> userNotFound() {
        LogInResponseDto responseBody=new LogInResponseDto(ResponseCode.ID_NOT_FOUND, ResponseMessage.ID_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> success(User user) {
        GetMyProfileResponseDto responseBody = new GetMyProfileResponseDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
