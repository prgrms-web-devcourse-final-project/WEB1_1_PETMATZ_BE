package com.petmatz.domain.chatting.component;

import com.petmatz.domain.chatting.repository.ChatRoomRepository;
import com.petmatz.domain.chatting.repository.UserToChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChatRoomDeleter {

    private final UserToChatRoomRepository userToChatRoomRepository;

    @Transactional
    public void deleteChatRoom(String roomId) {
        userToChatRoomRepository.deleteByChatRoom_Id(Long.valueOf(roomId));
    }
}
