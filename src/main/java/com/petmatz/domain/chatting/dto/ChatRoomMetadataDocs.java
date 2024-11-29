package com.petmatz.domain.chatting.dto;

import com.petmatz.common.constants.ChatMessageMsgType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "chat_room_metadata")
@Getter
public class ChatRoomMetadataDocs {
    @Id
    private String room_id;
    private String lastMessage;
    private LocalDateTime lastMessageTimestamp;
    private String msg_type;
    private int messageCount;
    private int unreadCount;

    @Builder
    public ChatRoomMetadataDocs(String room_id, String lastMessage, LocalDateTime lastMessageTimestamp, String msg_type, int messageCount, int unreadCount) {
        this.room_id = room_id;
        this.lastMessage = lastMessage;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.msg_type = msg_type;
        this.messageCount = messageCount;
        this.unreadCount = unreadCount;
    }




    public static ChatRoomMetadataDocs initChatRoomMetaData(ChatRoomInfo chatRoomInfo, long chatRoomId) {
        return ChatRoomMetadataDocs.builder()
                .room_id(String.valueOf(chatRoomId))
                .lastMessage("first")
                .lastMessageTimestamp(LocalDateTime.now())
                .messageCount(0)
                .unreadCount(0)
                .build();
    }

    public static ChatRoomMetadataDocs updateChatRoomMetaData(ChatMessageInfo chatMessageInfo, String chatRoomId) {
        return ChatRoomMetadataDocs.builder()
                .room_id(chatRoomId)
                .lastMessage(chatMessageInfo.getMsg())
                .lastMessageTimestamp(LocalDateTime.now())
                .messageCount(0)
                .unreadCount(0)
                .msg_type(chatMessageInfo.msg_type.name())
                .build();
    }
}
