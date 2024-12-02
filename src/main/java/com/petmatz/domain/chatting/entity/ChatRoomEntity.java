package com.petmatz.domain.chatting.entity;

import com.petmatz.domain.chatting.dto.ChatRoomListInfo;
import com.petmatz.domain.chatting.entity.UserToChatRoomEntity;
import com.petmatz.domain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChatRoomEntity extends BaseEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO 추후 userEntity와 연관관계 매핑이 필요함.
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserToChatRoomEntity> participants = new ArrayList<>();


    public void addParticipant(UserToChatRoomEntity participant) {
        this.participants.add(participant);
        participant.addChatRoom(this); // 연관관계 편의 메서드
    }

    public ChatRoomListInfo toChatRoomListInfo(String userName) {
        return ChatRoomListInfo.builder()
                .chatRoomId(String.valueOf(id))
                .chatRoomName(choseUserName(userName))
                .build();
    }

    private String choseUserName(String userName) {
        for (UserToChatRoomEntity participant : participants) {
            if (!participant.getUser().getAccountId().equals(userName)) {
                // 현재 사용자가 아닌 참여자의 이름 반환
                return participant.getUser().getAccountId();
            }
        }
        throw new IllegalArgumentException("No other participant found in the chat room for user: " + userName);
    }

}
