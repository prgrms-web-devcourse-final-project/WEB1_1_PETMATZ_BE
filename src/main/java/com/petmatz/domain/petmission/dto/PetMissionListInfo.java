package com.petmatz.domain.petmission.dto;

import com.petmatz.domain.petmission.entity.PetMissionAskEntity;
import com.petmatz.domain.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record PetMissionListInfo(
        LocalDateTime petMissionStarted,

        LocalDateTime petMissionEnd,
        PetMissionStatusZip status,



        List<PetMissionAskEntity> petMissionAsks

) {
}
