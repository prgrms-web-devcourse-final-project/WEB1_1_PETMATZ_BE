package com.petmatz.domain.chatting.dto;

import com.petmatz.common.constants.ChatMessageMsgType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ChatMessagePetMissionInfo(
        Long receiverId,
        List<String> petId,
        LocalDateTime missionStarted,
        LocalDateTime missionEnd,

        List<String> petMissionAskInfo

) {

    public ChatMessageInfo of(String receiverEmail) {
        return ChatMessageInfo.builder()
                .receiverEmail(receiverEmail)
                .msg("멍멍이 부탁 등록")
                .msg_type(ChatMessageMsgType.PLG)
                .msgTimestamp(LocalDateTime.now())
                .build();
    }
}
