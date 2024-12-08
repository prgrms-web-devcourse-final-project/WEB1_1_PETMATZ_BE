package com.petmatz.domain.chatting.entity;

import com.petmatz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UserToChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoomEntity chatRoom; // 채팅방

    public UserToChatRoomEntity() {
    }

    public void addUser(User user) {
        this.user = user;
        if (!user.getChatRooms().contains(this)) {
            user.getChatRooms().add(this); // 양방향 관계 설정
        }
    }

    public void addChatRoom(ChatRoomEntity chatRoom) {
        this.chatRoom = chatRoom;
        if (!chatRoom.getParticipants().contains(this)) {
            chatRoom.getParticipants().add(this); // 양방향 관계 설정
        }
    }
}
