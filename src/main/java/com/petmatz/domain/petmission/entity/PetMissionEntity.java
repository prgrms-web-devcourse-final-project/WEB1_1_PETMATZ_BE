package com.petmatz.domain.petmission.entity;

import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.petmission.dto.PetMissionInfo;
import com.petmatz.domain.petmission.dto.PetMissionStatusZip;
import com.petmatz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetMissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime petMissionStarted;

    private LocalDateTime petMissionEnd;

    @Enumerated(EnumType.STRING)
    private PetMissionStatusZip status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id", nullable = false)
    private User giver;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_mission_id")
    private List<PetMissionAskEntity> petMissionAsks = new ArrayList<>();

    @Builder
    public PetMissionEntity(LocalDateTime petMissionStarted, LocalDateTime petMissionEnd, PetMissionStatusZip status, Pet pet, User giver, List<PetMissionAskEntity> petMissionAsks) {
        this.petMissionStarted = petMissionStarted;
        this.petMissionEnd = petMissionEnd;
        this.status = status;
        this.pet = pet;
        this.giver = giver;
        this.petMissionAsks = petMissionAsks;
    }

    public static PetMissionEntity of(User user, PetMissionInfo petMissionInfo, Pet pet) {
        System.out.println("petMissionInfo.toString() :: " + petMissionInfo.toString());
        return PetMissionEntity.builder()
                .petMissionStarted(petMissionInfo.missionStarted())
                .petMissionEnd(petMissionInfo.missionEnd())
                .status(PetMissionStatusZip.fromDescription("시작"))
                .giver(user)
                .pet(pet)
                .petMissionAsks(
                        petMissionInfo.petMissionAskInfo().stream()
                                .map(PetMissionAskEntity::of)
                                .toList()
                )
                .build();
    }



}
