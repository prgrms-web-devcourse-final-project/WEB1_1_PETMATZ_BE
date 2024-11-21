package com.petmatz.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import com.petmatz.user.entity.User;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Getter
public class GetMypageResponseDto extends LogInResponseDto{
    private User user;

    public GetMypageResponseDto(User user) {
        super();
        this.user = user;
    }

    public static ResponseEntity<LogInResponseDto> userNotFound() {
        LogInResponseDto responseBody=new LogInResponseDto(ResponseCode.ID_NOT_FOUND, ResponseMessage.ID_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> success(User user) {
        GetMypageResponseDto responseBody = new GetMypageResponseDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
