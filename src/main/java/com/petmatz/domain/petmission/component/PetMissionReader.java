package com.petmatz.domain.petmission.component;


import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import com.petmatz.domain.petmission.repository.PetMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PetMissionReader {

    private final PetMissionRepository petMissionRepository;

    public List<UserToPetMissionEntity> selectPetMissionId(Long UUID) {
        Optional<List<UserToPetMissionEntity>> petMissionEntity = petMissionRepository.selectPetMissionList(String.valueOf(UUID));
        List<UserToPetMissionEntity> petMissionEntityList = petMissionEntity.get();
        return petMissionEntityList;
    }

}
