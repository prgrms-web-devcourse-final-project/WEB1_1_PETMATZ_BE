package com.petmatz.domain.petmission.repository;

import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetMissionRepository extends JpaRepository<UserToPetMissionEntity, Long> {

    @Query("select pm from UserToPetMissionEntity pm where pm.user.id = :userId")
    Optional<List<UserToPetMissionEntity>> selectPetMissionList(@Param("userId") String userId);

}
