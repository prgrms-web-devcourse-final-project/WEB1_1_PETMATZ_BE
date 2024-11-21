package com.petmatz.domain.chatting;

import com.petmatz.domain.chatting.dto.ChatRoomInfo;
import com.petmatz.domain.chatting.dto.ChatRoomListInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomProvider chatRoomProvider;
    private final ChatMessageProvider chatMessageProvider;
    private final ChatRoomReader chatRoomReader;

    public long createdChatRoom(ChatRoomInfo chatRoomInfo) {
        long chatRoomId = chatRoomProvider.append(chatRoomInfo);
        chatMessageProvider.init(chatRoomInfo,chatRoomId);
        return chatRoomId;
    }

    public List<ChatRoomListInfo> getChatRoomList(String userName) {
        return chatRoomReader.selectUserInChatRoomList(userName);
    }

}
