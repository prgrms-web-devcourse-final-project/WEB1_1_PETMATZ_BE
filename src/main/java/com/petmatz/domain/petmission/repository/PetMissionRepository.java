package com.petmatz.domain.petmission.repository;

import com.petmatz.domain.petmission.entity.PetMissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetMissionRepository extends JpaRepository<PetMissionEntity, Long> {
}
