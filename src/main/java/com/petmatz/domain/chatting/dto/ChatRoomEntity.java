package com.petmatz.domain.chatting.dto;

import com.petmatz.domain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomEntity extends BaseEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO 추후 userEntity와 연관관계 매핑이 필요함.
    private String user1;
    private String user2;
    //----


    @Builder
    public ChatRoomEntity(Long id, String user1, String user2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
    }

    public static ChatRoomEntity of(ChatRoomInfo chatRoomInfo) {
        return ChatRoomEntity.builder()
                .user1(chatRoomInfo.caregiverInfo())
                .user2(chatRoomInfo.entrustedInfo())
                .build();
    }
    public ChatRoomListInfo toChatRoomListInfo(String userName) {
        return ChatRoomListInfo.builder()
                .chatRoomId(String.valueOf(id))
                .chatRoomName(choseUserName(userName))
                .build();
    }

    private String choseUserName(String userName) {
        return userName.equals(user1) ? user1 : user2;
    }

}
