package com.petmatz.domain.user.entity;

import com.petmatz.domain.chatting.entity.UserToChatRoomEntity;
import com.petmatz.domain.global.BaseEntity;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import com.petmatz.domain.user.constant.*;
import com.petmatz.domain.user.info.EditMyProfileInfo;
import com.petmatz.domain.user.info.UpdateLocationInfo;
import com.petmatz.domain.user.info.UserInfo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "account_id", nullable = false, unique = true)
    private String accountId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
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

    @Column(name = "is_care_available", nullable = false)
    private Boolean isCareAvailable;

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

    @Column(name = "mbti", nullable = false)
    private String mbti;

    @Column(name="region")
    private String region;

    @Column(name="region_code")
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
        this.profileImg=info.getProfileImg();
        this.nickname=info.getNickname();
        this.introduction=info.getIntroduction();
        this.preferredSizes=info.getPreferredSizes();
        this.isCareAvailable=info.isCareAvailable();
    }

    public void updateLocation(UpdateLocationInfo info, String region, Integer regionCode){
        this.latitude=info.getLatitude();
        this.longitude=info.getLongitude();
        this.region=region;
        this.regionCode=regionCode;
    }

    public UserInfo of() {
        return UserInfo.builder()
                .id(id)
                .nickname(nickname)
                .email(accountId)
                .profileImg(profileImg)
                .build();
    }
}