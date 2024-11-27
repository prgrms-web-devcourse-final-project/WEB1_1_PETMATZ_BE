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

    public void insertPetMission(List<PetMissionEntity> petMissionEntityList) {
        List<PetMissionEntity> petMissionEntityList1 = petMissionRepository.saveAll(petMissionEntityList);
        for (PetMissionEntity petMissionEntity : petMissionEntityList1) {
            for (PetMissionAskEntity petMissionAsk : petMissionEntity.getPetMissionAsks()) {
                System.out.println(petMissionAsk.toString());
            }
        }
    }

}
