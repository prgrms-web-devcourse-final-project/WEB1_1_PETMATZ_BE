package com.petmatz.domain.chatting;

import com.petmatz.domain.chatting.dto.ChatRoomInfo;
import com.petmatz.domain.chatting.dto.ChatRoomListInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomAppend chatRoomAppend;
    private final ChatMessageAppend chatMessageAppend;
    private final ChatRoomReader chatRoomReader;

    public long createdChatRoom(ChatRoomInfo chatRoomInfo) {
        long chatRoomId = chatRoomAppend.append(chatRoomInfo);
        chatMessageAppend.init(chatRoomInfo,chatRoomId);
        return chatRoomId;
    }

    public List<ChatRoomListInfo> getChatRoomList(String userName) {
        return chatRoomReader.selectUserInChatRoomList(userName);
    }

}
