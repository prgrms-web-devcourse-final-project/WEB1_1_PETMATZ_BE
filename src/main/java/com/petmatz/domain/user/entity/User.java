package com.petmatz.domain.user.entity;

import com.petmatz.domain.aws.vo.S3Imge;
import com.petmatz.domain.chatting.entity.UserToChatRoomEntity;
import com.petmatz.domain.global.BaseEntity;
import com.petmatz.domain.match.exception.MatchException;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import com.petmatz.domain.user.constant.*;
import com.petmatz.domain.user.info.EditKakaoProfileInfo;
import com.petmatz.domain.user.info.EditMyProfileInfo;
import com.petmatz.domain.user.info.UpdateLocationInfo;
import com.petmatz.domain.user.info.UserInfo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.petmatz.domain.match.exception.MatchErrorCode.INSUFFICIENT_LATITUDE_DATA;
import static com.petmatz.domain.match.exception.MatchErrorCode.INSUFFICIENT_LONGITUDE_DATA;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "User")
@Table(name = "User")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_id",unique = true)
    private String accountId;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_img")
    private String profileImg;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_role")
    private LoginRole loginRole; // ROLE_USER, ROLE_ADMIN

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type")
    private LoginType loginType; // 'Normal', 'Kakao'

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender; // 'Male', 'Female'

    @Convert(converter = PreferredSizeConverter.class)
    @Column(name = "preferred_size")
    private List<PreferredSize> preferredSizes; // 'Small', 'Medium', 'Large', 'None'

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "is_care_available")
    private Boolean careAvailable;

    @Column(name = "is_registered")
    private Boolean isRegistered;

    @Column(name = "recommendation_count")
    private Integer recommendationCount;

    @Column(name = "care_completion_count")
    private Integer careCompletionCount;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "mbti")
    private String mbti;

    @Column(name = "region")
    private String region;

    @Column(name = "region_code")
    private Integer regionCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserToChatRoomEntity> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserToPetMissionEntity> userPetMissions = new ArrayList<>();

    public User checkUUID(List<User> users, Long id) {
        for (User user : users) {
            if (user.id.equals(id)) {
                return user;
            }
        }
        return null;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateRecommendation(Integer recommendationCount) {
        this.recommendationCount = recommendationCount;
    }

    public void updateProfile(EditMyProfileInfo info) {
        this.nickname = info.getNickname();
        this.introduction = info.getIntroduction();
        this.preferredSizes = info.getPreferredSizes();
        this.careAvailable = info.isCareAvailable();

    }

    public void updateLocation(UpdateLocationInfo info, String region, Integer regionCode) {
        this.latitude = info.getLatitude();
        this.longitude = info.getLongitude();
        this.region = region;
        this.regionCode = regionCode;
    }

    public void updateKakaoProfile(EditKakaoProfileInfo info, String region, Integer regionCode) {
        this.profileImg=info.getProfileImg();
        this.nickname=info.getNickname();
        this.introduction=info.getIntroduction();
        this.preferredSizes=info.getPreferredSizes();
        this.careAvailable=info.isCareAvailable();
        this.mbti=info.getMbti();
        this.gender=info.getGender();
        this.latitude=info.getLatitude();
        this.longitude=info.getLongitude();
        this.region=region;
        this.regionCode=regionCode;
    }


    public boolean checkImgURL(String Img) {
        return profileImg.equals(Img);
    }

    public UserInfo of() {
        return UserInfo.builder()
                .id(id)
                .nickname(nickname)
                .email(accountId)
                .profileImg(profileImg)
                .build();
    }

    public void checkLatitudeLongitude() { // (희수 : 예외나 위도 경도 범위 추후에 변경 예정입니다!)
        if (latitude <= 0) {
            throw new MatchException(INSUFFICIENT_LATITUDE_DATA);
        }
        if (longitude <= 0) {
            if (latitude == 0.0) {
                throw new MatchException(INSUFFICIENT_LATITUDE_DATA);
            }
            if (longitude == 0.0) {
                throw new MatchException(INSUFFICIENT_LONGITUDE_DATA);
            }
        }
    }

    //등록 상태로 변경
    public void updateUserRegistered() {
        if (Boolean.FALSE.equals(isRegistered)) {
            this.isRegistered = true;
        }
    }

    public String updateImgURL(String profileImgURL, S3Imge petImg) {
        if (!profileImg.equals(profileImgURL)) {
            profileImg = petImg.uploadURL();
            return petImg.checkResultImg();
        }
        return "";
    }
}
