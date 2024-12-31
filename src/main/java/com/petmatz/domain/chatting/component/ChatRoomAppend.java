package com.petmatz.domain.chatting.component;

import com.petmatz.domain.chatting.entity.ChatRoomEntity;
import com.petmatz.domain.chatting.dto.ChatRoomInfo;
import com.petmatz.domain.chatting.entity.UserToChatRoomEntity;
import com.petmatz.domain.chatting.repository.ChatRoomRepository;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ChatRoomAppend {

    private final ChatRoomRepository chatRoomRepository;

    private final UserRepository userRepository;

    //신규 채팅방 생성
    public long append(ChatRoomInfo chatRoomInfo) {
        User user1 = userRepository.findByAccountId(chatRoomInfo.caregiverInfo());
        User user2 = userRepository.findByAccountId(chatRoomInfo.entrustedInfo());


        UserToChatRoomEntity userToChatRoomEntity1 = new UserToChatRoomEntity();
        userToChatRoomEntity1.addUser(user1);

        UserToChatRoomEntity userToChatRoomEntity2 = new UserToChatRoomEntity();
        userToChatRoomEntity2.addUser(user2);

        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
        chatRoomEntity.addParticipant(userToChatRoomEntity1);
        chatRoomEntity.addParticipant(userToChatRoomEntity2);


        ChatRoomEntity saveChatRoomEntity = chatRoomRepository.save(chatRoomEntity);
        return saveChatRoomEntity.getId();
    }
}
