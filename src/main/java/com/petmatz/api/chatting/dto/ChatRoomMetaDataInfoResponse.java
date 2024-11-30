package com.petmatz.api.chatting.dto;

import com.petmatz.domain.chatting.dto.ChatRoomMetaDataInfo;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatRoomMetaDataInfoResponse(

        String chatRoomId,
        String lastMessage,
        LocalDateTime lastMessageTimestamp,
        int messageCount,
        int unreadCount,
        IChatUserResponse other

) {

    public static ChatRoomMetaDataInfoResponse of(ChatRoomMetaDataInfo chatRoomMetaDataInfo) {
        return ChatRoomMetaDataInfoResponse.builder()
                .chatRoomId(chatRoomMetaDataInfo.chatRoomId())
                .lastMessage(chatRoomMetaDataInfo.lastMessage())
                .lastMessageTimestamp(chatRoomMetaDataInfo.lastMessageTimestamp())
                .unreadCount(chatRoomMetaDataInfo.unreadCount())
                .other(IChatUserResponse.of(chatRoomMetaDataInfo.iChatUserInfo()))
                .build();
    }
}
