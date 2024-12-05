package com.petmatz.domain.chatting.dto;

import com.petmatz.common.constants.ChatMessageMsgType;
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

    ChatMessageMsgType msg_type;

    private boolean readStatus = true;


    @Builder
    public ChatMessageInfo(String senderEmail, String receiverEmail, String msg, LocalDateTime msgTimestamp, ChatMessageMsgType msg_type, boolean readStatus) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.msg = msg;
        this.msgTimestamp = msgTimestamp;
        this.msg_type = msg_type;
        this.readStatus = readStatus;
    }

//    public void changeReadStatus(boolean readStatus) {
//        this.readStatus = readStatus;
//    }

    public void changeReadStatus(LocalDateTime lastReadTimestamp) {
        this.readStatus = msgTimestamp.isBefore(lastReadTimestamp) || msgTimestamp.isEqual(lastReadTimestamp);
    }

    public void addSenderEmail(String addSenderEmail) {
        if (this.senderEmail == null || this.senderEmail.isEmpty()) {
            this.senderEmail = addSenderEmail;
        }
    }

}
