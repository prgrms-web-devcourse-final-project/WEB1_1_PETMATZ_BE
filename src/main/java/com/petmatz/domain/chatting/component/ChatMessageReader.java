package com.petmatz.domain.chatting.component;

import com.petmatz.domain.chatting.dto.ChatMessageInfo;
import com.petmatz.domain.chatting.docs.ChatReadStatusDocs;
import com.petmatz.domain.chatting.docs.ChatRoomDocs;
import com.petmatz.domain.chatting.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMessageReader {

    private final MongoTemplate mongoTemplate;
    
    //채팅방 목록에서 각 채팅방에 대한 도착한 메세지 갯수 조회
    public Integer countChatMessagesHistoryToLastDataAndUserName(String chatRoomsId,String user2Email,LocalDateTime lastReadTimestamp, int pageNumber, int pageSize) {
        Aggregation query = createQuerySelectChatMessagesPaging(chatRoomsId,user2Email,lastReadTimestamp, pageNumber, pageSize);

        AggregationResults<Document> aggregate =
                mongoTemplate.aggregate(query, "chat_rooms", Document.class);

        Document uniqueMappedResult = aggregate.getUniqueMappedResult();

        return uniqueMappedResult != null ? uniqueMappedResult.getInteger("messageCount") : 0;
    }

    //채팅 내역을 전부 조회, Page을 사용해 Page단위로 끊어서 조회 및 날짜순으로 정렬
    public List<ChatMessageInfo> selectChatMessagesHistory(String chatRoomsId, int pageNumber, int pageSize) {
        Aggregation query = createQuerySelectChatMessagesPaging(chatRoomsId, pageNumber, pageSize);

        AggregationResults<ChatMessageInfo> aggregate =
                mongoTemplate.aggregate(query, "chat_rooms", ChatMessageInfo.class);

        return aggregate.getMappedResults();
    }

    //해당 채팅 내역의 전체 메세지 갯수를 조회
    public long countChatMessages(String chatRoomId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(chatRoomId)),
                Aggregation.match(Criteria.where("messages").exists(true)),
                Aggregation.project().and("messages").size().as("messageCount")
        );

        AggregationResults<ChatRoomDocs> results = mongoTemplate.aggregate(aggregation, "chat_rooms", ChatRoomDocs.class);
        ChatRoomDocs result = results.getUniqueMappedResult();
        if (result == null || result.getMessages().isEmpty()) {
            return 0;
        }

        return result.getMessages().size();
    }


    //chat_read_status의 마지막 시간대를 조회, 조회대는 대상은 자기자신을 기준으로 상대방의 마지막 채팅 시간 조회
    public ChatReadStatusDocs selectChatMessageLastStatus(String chatRoomId, String userEmail) {
        Query query = new Query(Criteria.where("_id").is(ChatUtils.addString(chatRoomId,userEmail)).and("userEmail").is(userEmail));
        return mongoTemplate.findOne(query, ChatReadStatusDocs.class);
    }

    //----아래부터는 쿼리 생성 -----//

    protected Aggregation createQuerySelectChatMessagesPaging(String chatRoomsId, int pageNumber, int pageSize) {
        int skipCount = (pageNumber - 1) * pageSize; // 시작 위치 계산

        return Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(chatRoomsId)), // 특정 채팅방 필터
                Aggregation.unwind("messages"), // 배열의 각 메시지를 분리
                Aggregation.project("messages.senderEmail", "messages.receiverEmail", "messages.msg", "messages.msgTimestamp")
                        .and("messages.msgTimestamp").as("msgTimestamp"),
                Aggregation.sort(Sort.Direction.DESC, "msgTimestamp"), // 최신 메시지 정렬
                Aggregation.skip(skipCount), // 시작 위치
                Aggregation.limit(pageSize) // 한 페이지의 크기
        );
    }

    protected Aggregation createQuerySelectChatMessagesPaging(String chatRoomsId,String userEmail,LocalDateTime lastReadTimestamp, int pageNumber, int pageSize) {
        return Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(chatRoomsId)), // 특정 채팅방 필터
                Aggregation.unwind("messages"), // 메시지 배열의 각 항목 분리
                Aggregation.match(
                        Criteria.where("messages.msgTimestamp").gt(lastReadTimestamp)
                                .orOperator(
                                        Criteria.where("messages.receiverEmail").is(userEmail) // 특정 사용자와 연관된 메시지
                                )
                ),
                Aggregation.count().as("messageCount") // 메시지 개수 계산
        );
    };

}
