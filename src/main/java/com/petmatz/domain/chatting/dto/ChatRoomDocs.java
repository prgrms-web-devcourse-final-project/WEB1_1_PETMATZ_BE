package com.petmatz.domain.chatting.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomDocs {

    @Id
    // 채팅방 ID
    private String id;

    //보내는이 User Id
    //보내는 이의 Info 추가
    private String caregiverInfo;
    //받는이 User Id
    //받는 이의 Info 추가
    private String entrustedInfo;
    private List<ChatMessage> messages = new ArrayList<>();

    @Builder
    public ChatRoomDocs(String id, String caregiverInfo, String entrustedInfo, List<ChatMessage> messages) {
        this.id = id;
        this.caregiverInfo = caregiverInfo;
        this.entrustedInfo = entrustedInfo;
        this.messages = messages;
    }

    public static ChatRoomDocs initChatRoomDocs(ChatRoomInfo chatRoomInfo, long chatRoomID) {
        return ChatRoomDocs.builder()
                .id(String.valueOf(chatRoomID))
                .caregiverInfo(chatRoomInfo.caregiverInfo())
                .entrustedInfo(chatRoomInfo.entrustedInfo())
                .build();
    }

}
