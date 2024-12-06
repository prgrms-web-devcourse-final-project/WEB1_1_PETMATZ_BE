package com.petmatz.domain.petmission.component;

import com.petmatz.domain.petmission.entity.PetMissionAnswerEntity;
import com.petmatz.domain.petmission.repository.PetMissionAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetMissionInserter {

    private final PetMissionAnswerRepository petMissionAnswerRepository;

    public PetMissionAnswerEntity insertPetMissionAnswer(PetMissionAnswerEntity petMissionAnswerEntity) {
        return petMissionAnswerRepository.save(petMissionAnswerEntity);
    }



}
