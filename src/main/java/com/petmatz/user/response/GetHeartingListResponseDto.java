package com.petmatz.user.response;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.domain.user.entity.Heart;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetHeartingListResponseDto extends LogInResponseDto {
    private List<Heart> heartList;

    public GetHeartingListResponseDto(List<Heart> heartList) {
        super();
        this.heartList = heartList;
    }

    public static ResponseEntity<GetHeartingListResponseDto> success(List<Heart> heartList) {
        GetHeartingListResponseDto responseBody = new GetHeartingListResponseDto(heartList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
