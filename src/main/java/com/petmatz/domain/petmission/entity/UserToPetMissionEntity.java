package com.petmatz.domain.petmission.entity;

import com.petmatz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@Getter
public class UserToPetMissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_mission_id", nullable = false)
    private PetMissionEntity petMission;


    @Builder
    public UserToPetMissionEntity(User user, PetMissionEntity petMission) {
        this.user = user;
        this.petMission = petMission;
    }

    public static UserToPetMissionEntity of(User user, PetMissionEntity petMission) {
        return UserToPetMissionEntity.builder()
                .user(user)
                .petMission(petMission)
                .build();
    }

}
