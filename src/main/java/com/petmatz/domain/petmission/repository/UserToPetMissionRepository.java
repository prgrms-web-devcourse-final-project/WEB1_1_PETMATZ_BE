package com.petmatz.domain.petmission.repository;

import com.petmatz.domain.petmission.component.UserToPetMissionInserter;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserToPetMissionRepository extends JpaRepository<UserToPetMissionEntity, Long> {

    @Query("select utr from UserToPetMissionEntity utr where utr.user.id = :userId")
    Optional<List<UserToPetMissionEntity>> selectUserToPetMissionList(@Param("userId") Long userId);

    @Query("select utr from UserToPetMissionEntity utr where utr.petMission.id = :petMissionId")
    Optional<List<UserToPetMissionEntity>> selectUserToPetMissionList(@Param("petMissionId") String petMissionId);

    @Query("select utr from UserToChatRoomEntity  utr where utr.id = :petMissionId and utr.user.id = :userId")
    Optional<UserToPetMissionEntity> selectUserToPetMission(@Param("userId") Long userId,
                                @Param("petMissionId") String petMissionId);
}
