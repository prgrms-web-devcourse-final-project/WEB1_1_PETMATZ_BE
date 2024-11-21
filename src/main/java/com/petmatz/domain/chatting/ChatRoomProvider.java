package com.petmatz.domain.chatting;

import com.petmatz.common.exception.DomainException;
import com.petmatz.domain.chatting.dto.ChatRoomEntity;
import com.petmatz.domain.chatting.dto.ChatRoomInfo;
import com.petmatz.domain.chatting.repository.ChatRoomRepository;
import com.petmatz.domain.exception.DuplicationChatRoomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ChatRoomProvider {

    private final ChatRoomRepository chatRoomRepository;

    public long append(ChatRoomInfo chatRoomInfo) {
        Optional<ChatRoomEntity> chatRoom = chatRoomRepository.selectUser1AndUser2(chatRoomInfo.caregiverInfo(), chatRoomInfo.entrustedInfo());
        if (chatRoom.isPresent()) {
            throw DuplicationChatRoomException.EXCEPTION;
        }

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.of(chatRoomInfo);
        ChatRoomEntity saveChatRoomEntity = chatRoomRepository.save(chatRoomEntity);
        return saveChatRoomEntity.getId();
    }
}
