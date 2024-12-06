package com.petmatz.domain.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class EditMyProfileResponseDto extends LogInResponseDto {
    private String resultImgURL;

    public EditMyProfileResponseDto(String resultImgURL){
        super();
        this.resultImgURL=resultImgURL;
    }

    public static ResponseEntity<LogInResponseDto> idNotFound(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.ID_NOT_FOUND, ResponseMessage.ID_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> editFailed(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.EDIT_FAIL, ResponseMessage.EDIT_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<EditMyProfileResponseDto> success(String resultImgURL) { // 반환 타입 수정
        EditMyProfileResponseDto responseBody = new EditMyProfileResponseDto(resultImgURL);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
