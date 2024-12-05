package com.petmatz.domain.petmission.dto;

import com.petmatz.domain.petmission.entity.PetMissionAskEntity;
import lombok.Builder;

import java.util.List;

@Builder
public record PetMissionAskInfo(

        String petId,

        String comment
) {


}
