package com.petmatz.domain.chatting;

import com.petmatz.domain.chatting.dto.ChatRoomEntity;
import com.petmatz.domain.chatting.dto.ChatRoomListInfo;
import com.petmatz.domain.chatting.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChatRoomReader {

    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoomListInfo> selectUserInChatRoomList(String userName) {
        return chatRoomRepository.selectUserInChatRoomList(userName)
                .orElse(Collections.emptyList())
                .stream()
                .map(chatRoomEntity -> chatRoomEntity.toChatRoomListInfo(userName))
                .toList();
    }

}
