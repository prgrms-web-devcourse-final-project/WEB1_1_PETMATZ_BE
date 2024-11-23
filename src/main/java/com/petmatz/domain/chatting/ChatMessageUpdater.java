package com.petmatz.domain.chatting;

import com.petmatz.domain.chatting.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ChatMessageUpdater {

    private final MongoTemplate mongoTemplate;

    //메세지 추가하는 로직 ( 메세지는 insert, 메타 데이터는 new )
    public void updateMessage(ChatMessageInfo chatMessageInfo) {
        ChatMessage chatMessage = chatMessageInfo.of();

        Query selectChatRoomDocsQuery = new Query(Criteria.where("_id").is(chatMessageInfo.chatRoomId()));
        Query selectChatReadStatusDocs = new Query(Criteria.where("_id").is(addString(chatMessageInfo.chatRoomId(),chatMessage.getSenderId())));

        Update updateMessage = new Update().push("messages", chatMessage);
        Update updateChatReadStatus = new Update()
                .set("lastReadMessageId", chatMessageInfo.msg())
                .set("lastReadTimestamp",LocalDateTime.now());

        ChatRoomMetadataDocs chatRoomMetadataDocs = updateChatRoomMetaDataDocs(chatMessageInfo);

        mongoTemplate.updateFirst(selectChatRoomDocsQuery, updateMessage, ChatRoomDocs.class);
        mongoTemplate.updateFirst(selectChatReadStatusDocs, updateChatReadStatus, ChatReadStatusDocs.class);
        mongoTemplate.save(chatRoomMetadataDocs);
    }

    private ChatRoomMetadataDocs updateChatRoomMetaDataDocs(ChatMessageInfo chatMessageInfo) {
        return ChatRoomMetadataDocs.updateChatRoomMetaData(chatMessageInfo);
    }

    private static String addString(String chatRoomID, String userName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(chatRoomID);
        stringBuilder.append("_");
        stringBuilder.append(userName);
        return stringBuilder.toString();
    }

//    public void updateMessageStatus(ChatReadStatusInfo chatReadStatusInfo) {
//        Query query = makeQuery(chatReadStatusInfo);
//        Update update = makeUpdate(chatReadStatusInfo);
//        mongoTemplate.upsert(query, update, ChatReadStatusDocs.class);
//    }
//
//    private Query makeQuery(ChatReadStatusInfo chatReadStatusInfo) {
//        return new Query(Criteria.where("chatRoomId").is(chatReadStatusInfo.chatRoomId()).and("userId").is(chatReadStatusInfo.userId()));
//    }
//
//    private Update makeUpdate(ChatReadStatusInfo chatReadStatusInfo) {
//        return new Update()
//                .set("lastReadMessageId", chatReadStatusInfo.lastReadMessageData())
//                .set("lastReadTimestamp", LocalDateTime.now());
//    }
}
