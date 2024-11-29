package com.petmatz.domain.petmission.dto;

import com.petmatz.domain.petmission.entity.PetMissionAskEntity;
import lombok.Builder;

@Builder
public record PetMissionAskInfo(

        String comment
) {


}
