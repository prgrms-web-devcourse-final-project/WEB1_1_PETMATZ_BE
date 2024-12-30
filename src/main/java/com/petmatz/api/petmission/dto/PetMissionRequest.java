package com.petmatz.api.petmission.dto;

import com.petmatz.common.constants.ChatMessageMsgType;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;
import com.petmatz.domain.chatting.dto.ChatMessagePetMissionInfo;
import com.petmatz.domain.petmission.dto.PetMissionInfo;

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

    public ChatMessageInfo of(Long petMissionId, String careEmail, String receiverEmail) {
        return ChatMessageInfo.builder()
                .senderEmail(careEmail)
                .receiverEmail(receiverEmail)
                .msg(String.valueOf(petMissionId))
                .msgTimestamp(LocalDateTime.now())
                .msg_type(ChatMessageMsgType.PLG)
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
