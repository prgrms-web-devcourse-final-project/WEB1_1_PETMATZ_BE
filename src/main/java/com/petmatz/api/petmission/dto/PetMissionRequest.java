package com.petmatz.api.petmission.dto;

import com.petmatz.domain.chatting.dto.ChatMessagePetMissionInfo;
import com.petmatz.domain.petmission.dto.PetMissionInfo;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

public record PetMissionRequest(

//        //JWT 이용
//        Long careId,
        Long receiverId,
        List<String> petId,
        LocalDateTime missionStarted,
        LocalDateTime missionEnd,
        List<String> petMissionAsk

) {

    public PetMissionInfo of() {
        return PetMissionInfo.builder()
//                .careId(careId)
                .receiverId(receiverId)
                .petId(petId)
                .missionStarted(missionStarted)
                .missionEnd(missionEnd)
                .petMissionAskInfo(petMissionAsk)
                .build();
    }


    public ChatMessagePetMissionInfo ofto() {
        return ChatMessagePetMissionInfo.builder()
//                .careId(careId)
                .receiverId(receiverId)
                .petId(petId)
                .missionStarted(missionStarted)
                .missionEnd(missionEnd)
                .petMissionAskInfo(petMissionAsk)
                .build();
    }
}
