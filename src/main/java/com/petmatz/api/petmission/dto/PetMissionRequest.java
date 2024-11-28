package com.petmatz.api.petmission.dto;

import com.petmatz.domain.petmission.dto.PetMissionInfo;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

public record PetMissionRequest(

        Long careId,
        Long receiverId,
        String petId,
        LocalDateTime missionStarted,
        LocalDateTime missionEnd,
        List<PetMissionAsk> petMissionAsk

) {

    public PetMissionInfo of() {
        return PetMissionInfo.builder()
                .careId(careId)
                .receiverId(receiverId)
                .petId(petId)
                .missionStarted(missionStarted)
                .missionEnd(missionEnd)
                .petMissionAskInfo(petMissionAsk.stream()
                        .map(PetMissionAsk::of).toList())
                .build();
    }
}
