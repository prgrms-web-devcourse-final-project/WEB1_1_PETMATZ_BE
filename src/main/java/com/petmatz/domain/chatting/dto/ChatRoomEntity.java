package com.petmatz.domain.chatting.dto;

import com.petmatz.domain.global.BaseEntity;
import com.petmatz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomEntity extends BaseEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO 추후 userEntity와 연관관계 매핑이 필요함.
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserToChatRoomEntity> participants = new ArrayList<>();

    @Builder
    public ChatRoomEntity(List<UserToChatRoomEntity> participants) {
        this.participants = participants;
    }

    public static ChatRoomEntity of(ChatRoomInfo chatRoomInfo) {
        return ChatRoomEntity.builder()
                .participants()
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
