package com.petmatz.domain.chatting;

import com.petmatz.domain.chatting.dto.ChatMessage;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;

import com.petmatz.domain.chatting.dto.ChatReadStatusInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatDocsAppend chatDocsAppend;
    private final ChatMessageReader chatMessageReader;
//    private final ChatMessageCahing chatMessageCahing;
    private final ChatMessageUpdater chatMessageUpdater;

    public List<ChatMessage> selectMessage(String userId, String chatRoomsId, int pageNumber, int pageSize) {
        return chatMessageReader.selectChatMessages(userId, chatRoomsId, pageNumber,pageSize);
    }

    public void updateMessage(ChatMessageInfo chatMessageInfo) {
        chatMessageUpdater.updateMessage(chatMessageInfo);
//        chatMessageCahing.cachingChatMessage(chatMessageInfo);
    }

    public void updateMessageStatusRead(ChatReadStatusInfo chatReadStatusInfo) {
//        chatMessageUpdater.updateMessageStatus(chatReadStatusInfo);
    }
}
