package com.petmatz.domain.chatting.dto;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_read_status")
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
}
