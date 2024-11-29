package com.petmatz.domain.chatting.component;

import com.petmatz.domain.chatting.dto.ChatRoomDocs;
import com.petmatz.domain.chatting.dto.ChatRoomMetadataDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomMetaDataDeleter {

    private final MongoTemplate mongoTemplate;

    public void deleteChatRoomMetaDataDocs(String userName, String roomId) {
        Query query = new Query(Criteria.where("_id").is(roomId));
        mongoTemplate.remove(query, ChatRoomMetadataDocs.class);
    }
}
