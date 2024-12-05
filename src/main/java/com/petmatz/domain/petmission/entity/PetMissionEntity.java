package com.petmatz.domain.petmission.entity;

import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.petmission.dto.PetMissionInfo;
import com.petmatz.common.constants.PetMissionStatusZip;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "petMission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetToPetMissionEntity> petToPetMissions = new ArrayList<>();


    @OneToMany(mappedBy = "petMission", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PetMissionAskEntity> petMissionAsks = new ArrayList<>();


    @Builder
    public PetMissionEntity(LocalDateTime petMissionStarted, LocalDateTime petMissionEnd, PetMissionStatusZip status, List<PetToPetMissionEntity> petToPetMissions, List<PetMissionAskEntity> petMissionAsks) {
        this.petMissionStarted = petMissionStarted;
        this.petMissionEnd = petMissionEnd;
        this.status = status;
        this.petToPetMissions = new ArrayList<>();
        this.petMissionAsks =  new ArrayList<>();
    }


    public static PetMissionEntity of(PetMissionInfo petMissionInfo) {
        return PetMissionEntity.builder()
                .petMissionStarted(petMissionInfo.missionStarted())
                .petMissionEnd(petMissionInfo.missionEnd())
                .status(PetMissionStatusZip.fromDescription("시작"))
                .build();
    }

    public void addPetMissionAsk(List<PetMissionAskEntity> ask) {
        petMissionAsks.addAll(ask);
        ask.forEach(petMissionAskEntity -> petMissionAskEntity.addPetMission(this));
    }


    public void updatePetMissionStatusZip(PetMissionStatusZip updateStatus) {
        status = updateStatus;
    }

    public void addPetToPetMission(PetToPetMissionEntity pet) {
        this.petToPetMissions.add(pet);
    }

}
