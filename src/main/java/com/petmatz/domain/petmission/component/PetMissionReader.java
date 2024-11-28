package com.petmatz.domain.petmission.component;


import com.petmatz.domain.petmission.repository.PetMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetMissionReader {

    private final PetMissionRepository petMissionRepository;

    public void selectPetMissionId() {
//        petMissionRepository.fi
    }

}
