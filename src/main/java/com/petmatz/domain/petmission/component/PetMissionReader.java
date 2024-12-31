package com.petmatz.domain.petmission.component;


import com.petmatz.domain.petmission.entity.PetMissionAskEntity;
import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import com.petmatz.domain.petmission.repository.PetMissionAskRepository;
import com.petmatz.domain.petmission.repository.PetMissionRepository;
import com.petmatz.domain.petmission.repository.UserToPetMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PetMissionReader {

    private final PetMissionRepository petMissionRepository;
    private final PetMissionAskRepository petMissionAskRepository;


    public PetMissionEntity selectUserToPetMission(String petMissionId) {
        Optional<PetMissionEntity> petMissionEntity = petMissionRepository.selectUserToPetMission(petMissionId);
        return petMissionEntity.get();
    }

    public PetMissionAskEntity selectById(Long askId) {
        Optional<PetMissionAskEntity> byId = petMissionAskRepository.findById(askId);
        //TODO 에러 체크
        return byId.get();
    }

}
