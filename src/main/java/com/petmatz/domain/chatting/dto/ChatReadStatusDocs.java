package com.petmatz.domain.chatting.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_read_status")
@Getter
public class ChatReadStatusDocs {

    @Id
    private String chatRoomId;
    private String userId;
    private String lastReadMessageId;
    private LocalDateTime lastReadTimestamp;

    @Builder
    public ChatReadStatusDocs(String chatRoomId, String userId, String lastReadMessageId, LocalDateTime lastReadTimestamp) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.lastReadMessageId = lastReadMessageId;
        this.lastReadTimestamp = lastReadTimestamp;
    }

    public static ChatReadStatusDocs initChatReadStatusData(String userName,long chatRoomID) {
        return ChatReadStatusDocs.builder()
                .chatRoomId(addString(chatRoomID, userName))
                .userId(userName)
                .build();
    }

    private static String addString(long chatRoomID, String userName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(chatRoomID);
        stringBuilder.append("_");
        stringBuilder.append(userName);
        return stringBuilder.toString();
    }
}
