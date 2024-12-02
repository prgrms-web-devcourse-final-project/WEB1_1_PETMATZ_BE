package com.petmatz.api.chatting.dto;

import com.petmatz.common.constants.ChatMessageMsgType;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessage(
        String senderId,

        String receiverId,

        String msg,

        Boolean readStatus,

        ChatMessageMsgType msg_type,

        LocalDateTime msgTimestamp
) {
    public static ChatMessage of(ChatMessageInfo chatMessageInfo) {
        return ChatMessage.builder()
                .senderId(chatMessageInfo.getSenderEmail())
                .receiverId(chatMessageInfo.getReceiverEmail())
                .msg(chatMessageInfo.getMsg())
                .msg_type(chatMessageInfo.getMsg_type())
                .msgTimestamp(chatMessageInfo.getMsgTimestamp())
                .readStatus(chatMessageInfo.isReadStatus())
                .build();
    }
}
