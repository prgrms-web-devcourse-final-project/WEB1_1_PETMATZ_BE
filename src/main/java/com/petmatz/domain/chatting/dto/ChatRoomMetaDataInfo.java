package com.petmatz.domain.chatting.dto;

import com.petmatz.domain.chatting.docs.ChatRoomMetadataDocs;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatRoomMetaDataInfo(
        String lastMessage,
        LocalDateTime lastMessageTimestamp,
        int messageCount,
        int unreadCount,
        IChatUserInfo iChatUserInfo
) {

    public static ChatRoomMetaDataInfo of(ChatRoomMetadataDocs chatRoomMetadataDocs, int messageCount, int unreadCount, IChatUserInfo iChatUserInfo) {
        return ChatRoomMetaDataInfo.builder()
                .lastMessage(chatRoomMetadataDocs.getLastMessage())
                .lastMessageTimestamp(chatRoomMetadataDocs.getLastMessageTimestamp())
                .messageCount(messageCount)
                .unreadCount(unreadCount)
                .iChatUserInfo(iChatUserInfo)
                .build();
    }


}
