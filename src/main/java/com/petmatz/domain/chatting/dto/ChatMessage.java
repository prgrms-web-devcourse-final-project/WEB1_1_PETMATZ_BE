package com.petmatz.domain.chatting.dto;

import co.elastic.clients.util.DateTime;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ChatMessage {



    private String senderId;

    private String receiverId;

    private String msg;

    private LocalDateTime msgTimestamp;

    private boolean readStatus;

    @Builder
    public ChatMessage(String message1, String chatRoomId, String senderId, String receiverId, String msg, LocalDateTime msgTimestamp, boolean readStatus) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.msg = msg;
        this.msgTimestamp = msgTimestamp;
        this.readStatus = readStatus;
    }
}
