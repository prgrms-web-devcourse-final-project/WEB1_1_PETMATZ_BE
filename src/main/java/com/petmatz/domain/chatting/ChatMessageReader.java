package com.petmatz.domain.chatting;

import com.petmatz.domain.chatting.dto.ChatMessage;
import com.petmatz.domain.chatting.dto.ChatRoomDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMessageReader {

//    private final static int PAGE_SIZE = 15;

    private final MongoTemplate mongoTemplate;

    public List<ChatMessage> selectChatMessages(String chatRoomsId, int pageNumber, int pageSize) {
        Aggregation query = createQuerySelectChatMessagesPaging(chatRoomsId, pageNumber, pageSize);

        AggregationResults<ChatMessage> aggregate =
                mongoTemplate.aggregate(query, "chat_rooms", ChatMessage.class);

        return aggregate.getMappedResults();
    }

    private Aggregation createQuerySelectChatMessagesPaging(String chatRoomsId, int pageNumber, int pageSize) {
        return Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(chatRoomsId)),
                Aggregation.unwind("messages"),
                Aggregation.project("messages.senderId", "messages.receiverId", "messages.msg", "messages.msgTimestamp")
                        .and("messages.msgTimestamp").as("msgTimestamp"),
                Aggregation.sort(Sort.Direction.DESC, "msgTimestamp"),
                Aggregation.skip((long) (pageNumber - 1) * pageSize),
                Aggregation.limit(pageSize)
        );
    }

}
