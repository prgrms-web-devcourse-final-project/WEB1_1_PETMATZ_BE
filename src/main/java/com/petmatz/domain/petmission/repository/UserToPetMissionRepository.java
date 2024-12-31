package com.petmatz.domain.petmission.repository;

import com.petmatz.domain.petmission.component.UserToPetMissionInserter;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserToPetMissionRepository extends JpaRepository<UserToPetMissionEntity, Long> {

    @Query("select utr from UserToPetMissionEntity utr where utr.user.id = :userId")
    Optional<List<UserToPetMissionEntity>> selectUserToPetMissionList(@Param("userId") Long userId);


    @Query("SELECT utr FROM UserToPetMissionEntity utr " +
            "WHERE utr.user.id = :userId " +
            "AND DATE(utr.petMission.petMissionStarted) <= :currentDate " +
            "AND DATE(utr.petMission.petMissionEnd) >= :currentDate")
    List<UserToPetMissionEntity> selectUserToPetMissionList(
            @Param("userId") Long userId,
            @Param("currentDate") LocalDate currentDate);



    @Query("select utr from UserToPetMissionEntity utr where utr.petMission.id = :petMissionId")
    Optional<List<UserToPetMissionEntity>> selectUserToPetMissionList(@Param("petMissionId") String petMissionId);


//    @Query("select pm from UserToPetMissionEntity pm where pm.user.id = :userId")
//    Optional<List<UserToPetMissionEntity>> selectPetMissionList(@Param("userId") String userId);


}
