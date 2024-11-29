package com.petmatz.domain.petmission.repository;

import com.petmatz.domain.petmission.entity.PetMissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetMissionRepository extends JpaRepository<PetMissionEntity, Long> {

    @Query("select pet from PetMissionEntity pet where pet.giver.id = :userId")
    Optional<List<PetMissionEntity>> selectPetMissionList(@Param("userId") String userId);

}
