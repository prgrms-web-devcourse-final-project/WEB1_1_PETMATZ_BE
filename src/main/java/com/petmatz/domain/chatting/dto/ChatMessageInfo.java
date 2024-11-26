package com.petmatz.domain.chatting.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ChatMessageInfo {


    private String senderEmail;

    private String receiverEmail;

    private String msg;

    private LocalDateTime msgTimestamp;


    private boolean readStatus = true;


    @Builder
    public ChatMessageInfo(String senderEmail, String receiverEmail, String msg, LocalDateTime msgTimestamp, boolean readStatus) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.msg = msg;
        this.msgTimestamp = msgTimestamp;
        this.readStatus = readStatus;
    }

    public void changeReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }



}
