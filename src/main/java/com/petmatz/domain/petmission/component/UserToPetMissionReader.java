package com.petmatz.domain.petmission.component;

import com.petmatz.api.petmission.dto.PetMissionUpdateRequest;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import com.petmatz.domain.petmission.repository.UserToPetMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserToPetMissionReader {

    private final UserToPetMissionRepository userToPetMissionRepository;

    //TODO 예외 처리 필요
    public List<UserToPetMissionEntity> selectUserToPetMissionList(Long userId) {
        Optional<List<UserToPetMissionEntity>> userToPetMissionEntities = userToPetMissionRepository.selectUserToPetMissionList(userId);
        return userToPetMissionEntities.get();
    }

    public List<UserToPetMissionEntity> selectUserToPetMissionList(PetMissionUpdateRequest petMissionUpdateRequest) {
        Optional<List<UserToPetMissionEntity>> userToPetMissionEntities = userToPetMissionRepository.selectUserToPetMissionList(petMissionUpdateRequest.petMissionId());
        return userToPetMissionEntities.get();
    }

    public UserToPetMissionEntity selectUserToPetMission(String petMissionId, Long userId) {
        Optional<UserToPetMissionEntity> userToPetMissionEntity = userToPetMissionRepository.selectUserToPetMission(petMissionId, userId);
        return userToPetMissionEntity.get();
    }
}
