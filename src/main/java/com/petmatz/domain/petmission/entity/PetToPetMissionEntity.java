package com.petmatz.domain.petmission.entity;

import com.petmatz.domain.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PetToPetMissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_mission_id", nullable = false)
    private PetMissionEntity petMission;

    @Builder
    public PetToPetMissionEntity(Pet pet, PetMissionEntity petMission) {
        this.pet = pet;
        this.petMission = petMission;
    }

    public static PetToPetMissionEntity of(Pet pet, PetMissionEntity petMissionEntity) {
        return PetToPetMissionEntity.builder()
                .pet(pet)
                .petMission(petMissionEntity)
                .build();
    }
}
