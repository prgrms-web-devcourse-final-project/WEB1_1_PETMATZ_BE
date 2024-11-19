package com.petmatz.user.entity;

import com.petmatz.domain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "User")
@Table(name = "User")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "account_id", nullable = false, unique = true)
    private String accountId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "role", nullable = false, length = 10)
    private String loginRole; // ROLE_USER, ROLE_ADMIN

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role; // Enum type for role: 'Dol' or 'Mat'

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType; // Enum type for login_type: 'Normal', 'Kakao'

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender; // Enum type for gender: 'Male', 'Female'

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_size")
    private PreferredSize preferredSize; // Enum type for preferred_size: 'Small', 'Medium', 'Large'

    @Column(name = "introduction", nullable = false)
    private String introduction;

    @Column(name = "is_care_available", nullable = false)
    private Boolean isCareAvailable = false;

    @Column(name = "is_registered", nullable = false)
    private Boolean isRegistered = false;

    @Column(name = "recommendation_count", nullable = false)
    private Integer recommendationCount = 0;

    @Column(name = "care_completion_count", nullable = false)
    private Integer careCompletionCount = 0;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public enum Role {
        Dol, Mat
    }

    public enum LoginType {
        Normal, Kakao
    }

    public enum Gender {
        Male, Female
    }

    public enum PreferredSize {
        Small, Medium, Large
    }
}