package com.petmatz.domain.chatting;

import com.petmatz.domain.chatting.dto.ChatMessage;
import com.petmatz.domain.chatting.dto.ChatReadStatusDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMessageReader {

//    private final static int PAGE_SIZE = 15;

    private final MongoTemplate mongoTemplate;

    public List<ChatMessage> selectChatMessages(String userName, String chatRoomsId, int pageNumber, int pageSize) {
        System.out.println(userName);
        System.out.println(chatRoomsId);
        List<ChatMessage> chatMessages = selectChatMessagesHistory(chatRoomsId, pageNumber, pageSize);


        ChatReadStatusDocs chatReadStatusDocs = createQuerySelectChatMessageLastStatus(chatRoomsId, userName);
        System.out.println(chatReadStatusDocs.toString());


        LocalDateTime localDateTime =chatReadStatusDocs.getLastReadTimestamp();
        System.out.println("lastReadMessageId :: " + localDateTime);
        if (localDateTime == null) {
            return new ArrayList<>();
        }
//        chatMessages.forEach(message -> {
//            boolean isRead = lastReadMessageId != null && message.getId().compareTo(lastReadMessageId) <= 0;
//            message.setIsRead(isRead); // 읽음 상태를 DTO나 Response 객체에 설정
//        });

        return null;
    }

    private void selectMessageStatus() {

    }

    private List<ChatMessage> selectChatMessagesHistory(String chatRoomsId, int pageNumber, int pageSize) {
        Aggregation query = createQuerySelectChatMessagesPaging(chatRoomsId, pageNumber, pageSize);

        AggregationResults<ChatMessage> aggregate =
                mongoTemplate.aggregate(query, "chat_rooms", ChatMessage.class);

        return aggregate.getMappedResults();
    }

    private ChatReadStatusDocs createQuerySelectChatMessageLastStatus(String chatRoomId, String userName) {
        Query query = new Query(Criteria.where("_id").is(addString(chatRoomId,userName)).and("userId").is(userName));
        return mongoTemplate.findOne(query, ChatReadStatusDocs.class);
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

    private static String addString(String chatRoomID, String userName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(chatRoomID);
        stringBuilder.append("_");
        stringBuilder.append(userName);
        return stringBuilder.toString();
    }

}
