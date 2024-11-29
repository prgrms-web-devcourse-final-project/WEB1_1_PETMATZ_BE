package com.petmatz.domain.petmission.repository;

import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserToPetMissionRepository extends JpaRepository<UserToPetMissionEntity, Long> {
}
