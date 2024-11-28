package com.petmatz.domain.petmission.component;

import com.petmatz.domain.petmission.entity.PetMissionAskEntity;
import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.petmission.repository.PetMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PetMissionInseart {

    private final PetMissionRepository petMissionRepository;

    public List<PetMissionEntity> insertPetMission(List<PetMissionEntity> petMissionEntityList) {
         return petMissionRepository.saveAll(petMissionEntityList);
    }

}
