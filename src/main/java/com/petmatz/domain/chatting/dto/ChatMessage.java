package com.petmatz.domain.chatting.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ChatMessage {



    private String senderId;

    private String receiverId;

    private String msg;

    private LocalDateTime msgTimestamp;


    @Builder
    public ChatMessage(String message1, String chatRoomId, String senderId, String receiverId, String msg, LocalDateTime msgTimestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.msg = msg;
        this.msgTimestamp = msgTimestamp;
    }



}
