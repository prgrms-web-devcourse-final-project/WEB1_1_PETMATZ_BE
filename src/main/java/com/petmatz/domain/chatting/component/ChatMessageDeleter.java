package com.petmatz.domain.chatting.component;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageDeleter {

    private final MongoTemplate mongoTemplate;

    public void deleteChatMessageDocs() {
        Query query = new Query(Criteria.where("fieldName").is("value"));
        mongoTemplate.remove(query, "collectionName");
    }

    public void deleteDocs() {

    }
}
