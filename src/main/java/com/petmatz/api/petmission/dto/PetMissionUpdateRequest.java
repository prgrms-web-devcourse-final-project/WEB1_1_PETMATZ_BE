package com.petmatz.api.petmission.dto;

import com.petmatz.common.constants.PetMissionStatusZip;

public record PetMissionUpdateRequest(

        String petMissionId,
        PetMissionStatusZip missionStatusZip

) {
}
