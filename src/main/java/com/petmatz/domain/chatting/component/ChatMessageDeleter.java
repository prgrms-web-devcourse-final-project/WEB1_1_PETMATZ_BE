package com.petmatz.domain.chatting.component;

import com.petmatz.domain.chatting.dto.ChatRoomDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageDeleter {

    private final MongoTemplate mongoTemplate;

    public void deleteChatMessageDocs(String userName, String roomId) {
        Query query = new Query(Criteria.where("_id").is(roomId));
        mongoTemplate.remove(query, ChatRoomDocs.class);
    }
}
