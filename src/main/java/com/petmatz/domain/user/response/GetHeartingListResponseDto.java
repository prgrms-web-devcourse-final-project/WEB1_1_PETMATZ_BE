package com.petmatz.domain.user.response;

import com.petmatz.api.user.request.HeartedUserDto;
import com.petmatz.user.common.LogInResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetHeartingListResponseDto extends LogInResponseDto {
    private List<HeartedUserDto> heartedUsers;

    public GetHeartingListResponseDto(List<HeartedUserDto> heartedUsers) {
        super();
        this.heartedUsers = heartedUsers;
    }

    public static ResponseEntity<GetHeartingListResponseDto> success(List<HeartedUserDto> heartedUsers) {
        GetHeartingListResponseDto responseBody = new GetHeartingListResponseDto(heartedUsers);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}