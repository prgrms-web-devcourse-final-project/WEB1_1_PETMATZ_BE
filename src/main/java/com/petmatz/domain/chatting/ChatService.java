package com.petmatz.domain.chatting;

import com.petmatz.api.chatting.dto.ChatReadRequest;
import com.petmatz.domain.chatting.dto.ChatMessage;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageAppend chatMessageAppend;
    private final ChatMessageReader chatMessageReader;
    private final ChatMessageCahing chatMessageCahing;
    private final ChatMessageUpdater chatMessageUpdater;

    public List<ChatMessage> selectMessage(String userId, String chatRoomsId, int pageNumber, int pageSize) {
        return chatMessageReader.selectChatMessages(userId, chatRoomsId, pageNumber,pageSize);
    }

    public void saveChat(ChatMessageInfo chatMessageInfo) {
        chatMessageAppend.append(chatMessageInfo);
//        chatMessageCahing.cachingChatMessage(chatMessageInfo);
    }

    public void updateMessageStatusRead(ChatReadRequest chatReadRequest) {
        chatMessageUpdater.updateMessageStatus(chatReadRequest);
    }
}
