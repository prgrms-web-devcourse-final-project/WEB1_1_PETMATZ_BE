package com.petmatz.api.petmission.dto;

import com.petmatz.common.constants.ChatMessageMsgType;
import com.petmatz.common.constants.PetMissionStatusZip;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;

import java.time.LocalDateTime;

public record PetMissionUpdateRequest(

        String careEmail,
        String receiverEmail,
        String petMissionId,
        PetMissionStatusZip missionStatusZip

) {

    public ChatMessageInfo of(String petMissionId) {
        return ChatMessageInfo.builder()
                .senderEmail(careEmail)
                .receiverEmail(receiverEmail)
                .msg(petMissionId)
                .msgTimestamp(LocalDateTime.now())
                .msg_type(ChatMessageMsgType.END)
                .build();
    }
}
