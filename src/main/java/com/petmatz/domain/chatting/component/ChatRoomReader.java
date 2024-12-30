package com.petmatz.domain.chatting.component;

import com.petmatz.domain.chatting.dto.ChatRoomInfo;
import com.petmatz.domain.chatting.entity.ChatRoomEntity;
import com.petmatz.domain.chatting.entity.UserToChatRoomEntity;
import com.petmatz.domain.chatting.repository.ChatRoomRepository;
import com.petmatz.domain.chatting.repository.UserToChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChatRoomReader {

    private final ChatRoomRepository chatRoomRepository;
    private final UserToChatRoomRepository userToChatRoomRepository;

    //TODO 예외처리 해야 함.
    //자기자신이 속한 채팅방을 전부 조회
    public List<UserToChatRoomEntity> findChatRoomNumber(String userEmail) {
        Optional<List<UserToChatRoomEntity>> chatRoomEntities = userToChatRoomRepository.findByUser_AccountId(userEmail);
        System.out.println(chatRoomEntities.get());
        return chatRoomEntities.get();
    }

    public Optional<ChatRoomEntity> selectChatRoom(ChatRoomInfo chatRoomInfo) {
        return chatRoomRepository.findChatRoomByUsers(chatRoomInfo.caregiverInfo(), chatRoomInfo.entrustedInfo());
    }

    public List<String> selectChatRoomUserList(String chatRoomId) {
        return userToChatRoomRepository.selectChatRoomForUserList(Long.valueOf(chatRoomId));
    }


}
