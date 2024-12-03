package com.petmatz.domain.chatting.component;

import com.petmatz.domain.chatting.docs.ChatReadStatusDocs;
import com.petmatz.domain.chatting.dto.ChatReadStatusInfo;
import com.petmatz.domain.chatting.utils.ChatUtils;
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


    public void updateMessageStatusTime(String chatRoomId, String userEmail) {
        Query query = makeQuery(chatRoomId, userEmail);
        Update update = makeUpdate();
        mongoTemplate.updateFirst(query, update, ChatReadStatusDocs.class);
    }

    private Query makeQuery(String chatRoomId, String userEmail) {
        return new Query(Criteria.where("_id").is(ChatUtils.addString(chatRoomId,userEmail)).and("userEmail").is(userEmail));
    }


    private Update makeUpdate() {
        return new Update()
                .set("lastReadTimestamp", LocalDateTime.now());
    }
}
