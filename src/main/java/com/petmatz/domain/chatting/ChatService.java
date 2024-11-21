package com.petmatz.domain.chatting;

import com.petmatz.domain.chatting.dto.ChatMessage;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;
import com.petmatz.domain.chatting.dto.ChatRoomDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageProvider chatMessageProvider;
    private final ChatMessageReader chatMessageReader;

    public List<ChatMessage> selectMessage(String chatRoomsId, int pageNumber, int pageSize) {
        return chatMessageReader.selectChatMessages(chatRoomsId, pageNumber,pageSize);
    }

    public void saveChat(ChatMessageInfo chatMessageInfo) {
        chatMessageProvider.append(chatMessageInfo);
    }

}
