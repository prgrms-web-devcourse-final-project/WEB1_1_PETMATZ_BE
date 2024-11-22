package com.petmatz.domain.chatting;

import com.petmatz.api.chatting.dto.ChatReadRequest;
import com.petmatz.domain.chatting.dto.ChatReadStatusDocs;
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

    public void updateMessageStatus(ChatReadRequest chatReadRequest) {
        Query query = makeQuery(chatReadRequest);
        Update update = makeUpdate(chatReadRequest);
        mongoTemplate.upsert(query, update, ChatReadStatusDocs.class);
    }

    private Query makeQuery(ChatReadRequest chatReadRequest) {
        return new Query(Criteria.where("chatRoomId").is(chatReadRequest.chatRoomId()).and("userId").is(chatReadRequest.userId()));
    }

    private Update makeUpdate(ChatReadRequest chatReadRequest) {
        return new Update()
                .set("lastReadMessageId", chatReadRequest.lastReadMessageId())
                .set("lastReadTimestamp", LocalDateTime.now());
    }
}
