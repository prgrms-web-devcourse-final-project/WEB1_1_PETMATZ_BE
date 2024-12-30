package com.petmatz.domain.chatting.docs;

import com.petmatz.domain.chatting.utils.ChatUtils;
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
    private String userEmail;
    private String lastReadMessageId;
    private LocalDateTime lastReadTimestamp;

    @Builder
    public ChatReadStatusDocs(String chatRoomId, String userEmail, String lastReadMessageId, LocalDateTime lastReadTimestamp) {
        this.chatRoomId = chatRoomId;
        this.userEmail = userEmail;
        this.lastReadMessageId = lastReadMessageId;
        this.lastReadTimestamp = lastReadTimestamp;
    }

    public static ChatReadStatusDocs initChatReadStatusData(String userEmail,long chatRoomID) {
        return ChatReadStatusDocs.builder()
                .lastReadTimestamp(LocalDateTime.now())
                .chatRoomId(ChatUtils.addString(String.valueOf(chatRoomID), userEmail))
                .userEmail(userEmail)
                .build();
    }

    public LocalDateTime checkLastReadTimestamp() {
        return this.chatRoomId != null ? this.lastReadTimestamp : null;
    }

}
