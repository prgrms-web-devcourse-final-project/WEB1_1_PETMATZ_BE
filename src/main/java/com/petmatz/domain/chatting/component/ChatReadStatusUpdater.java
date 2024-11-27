package com.petmatz.domain.chatting.component;

import com.petmatz.domain.chatting.dto.ChatReadStatusDocs;
import com.petmatz.domain.chatting.dto.ChatReadStatusInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ChatReadStatusUpdater {

    private final MongoTemplate mongoTemplate;

    //TODO 현재는 사용하지 않음.
    public void updateMessageStatus(ChatReadStatusInfo chatReadStatusInfo) {
        Query query = makeQuery(chatReadStatusInfo);
        Update update = makeUpdate(chatReadStatusInfo);
        mongoTemplate.upsert(query, update, ChatReadStatusDocs.class);
    }

    private Query makeQuery(ChatReadStatusInfo chatReadStatusInfo) {
        return new Query(Criteria.where("_Id").is(chatReadStatusInfo.chatRoomId()).and("userId").is(chatReadStatusInfo.userEmail()));
    }

    private Update makeUpdate(ChatReadStatusInfo chatReadStatusInfo) {
        return new Update()
                .set("lastReadMessageId", chatReadStatusInfo.lastReadMessageData())
                .set("lastReadTimestamp", LocalDateTime.now());
    }
}
