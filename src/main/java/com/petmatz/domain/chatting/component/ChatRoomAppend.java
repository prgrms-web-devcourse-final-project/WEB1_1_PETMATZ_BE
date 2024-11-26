package com.petmatz.domain.chatting.component;

import com.petmatz.domain.chatting.dto.ChatRoomEntity;
import com.petmatz.domain.chatting.dto.ChatRoomInfo;
import com.petmatz.domain.chatting.repository.ChatRoomRepository;
import com.petmatz.domain.exception.DuplicationChatRoomException;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ChatRoomAppend {

    private final ChatRoomRepository chatRoomRepository;

    private final UserRepository userRepository;

    //신규 채팅방 생성
    public long append(ChatRoomInfo chatRoomInfo) {
        userRepository.findById()

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.of(chatRoomInfo);
        ChatRoomEntity saveChatRoomEntity = chatRoomRepository.save(chatRoomEntity);
        return saveChatRoomEntity.getId();
    }
}