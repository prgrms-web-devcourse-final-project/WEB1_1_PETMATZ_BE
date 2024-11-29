package com.petmatz.domain.user.response;

import com.petmatz.domain.user.entity.User;
import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.ResponseCode;
import com.petmatz.user.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetMyProfileResponseDto extends LogInResponseDto {
    private Long id;
    private String accountId;
    private String nickname;
    private String profileImg;
    private User.Role role;
    private String preferredSize;
    private User.Gender gender;
    private Boolean isRegistered;
    private Integer recommendationCount;
    private Integer careCompletionCount;
    private Boolean isCareAvailable;
    private Double latitude;
    private Double longitude;
    private String mbti;
    private String region;

    public GetMyProfileResponseDto(User user) {
        super();
        this.id = user.getId();
        this.accountId = user.getAccountId();
        this.nickname = user.getNickname();
        this.profileImg=user.getProfileImg();
        this.role = user.getRole();
        this.preferredSize =user.getPreferredSize();
        this.gender = user.getGender();
        this.isRegistered = user.getIsRegistered();
        this.recommendationCount = user.getRecommendationCount();
        this.careCompletionCount = user.getCareCompletionCount();
        this.isCareAvailable = user.getIsCareAvailable();
        this.latitude = user.getLatitude();
        this.longitude = user.getLongitude();
        this.mbti=user.getMbti();
        this.region=user.getRegion();
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
