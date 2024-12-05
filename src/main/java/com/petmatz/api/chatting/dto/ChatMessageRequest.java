package com.petmatz.api.chatting.dto;

import com.petmatz.common.constants.ChatMessageMsgType;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;

import java.time.LocalDateTime;

public record ChatMessageRequest(


        String chatRoomId,

        String senderEmail,

        String receiverEmail,

        String msg,

        ChatMessageMsgType msg_type


) {

    public ChatMessageInfo of() {
        return ChatMessageInfo.builder()
                .senderEmail(senderEmail)
                .receiverEmail(receiverEmail)
                .msg(msg)
                .msgTimestamp(LocalDateTime.now())
                .msg_type(ChatMessageMsgType.MSG)
                .build();
    }

}
