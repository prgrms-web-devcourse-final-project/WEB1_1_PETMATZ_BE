package com.petmatz.domain.chatting.component;

import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.chatting.docs.ChatReadStatusDocs;
import com.petmatz.domain.chatting.docs.ChatRoomDocs;
import com.petmatz.domain.chatting.docs.ChatRoomMetadataDocs;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;
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
public class ChatMessageUpdater {

    private final MongoTemplate mongoTemplate;
    private final JwtExtractProvider jwtExtractProvider;

    //메세지 추가하는 로직 ( 메세지는 업데이트 ( 바뀐 요소만 ), 메타 데이터는 new해서 덮어씌우기, chat_read_status도 업데이트 ( 바뀐 요소만 ) )
    public void updateMessage(ChatMessageInfo chatMessageInfo, String chatRoomId) {
        String getSenderEmail = jwtExtractProvider.findAccountIdFromJwt();
        chatMessageInfo.addSenderEmail(getSenderEmail);
        System.out.println("chatMessageInfo.getMsg_type().toString() : " + chatMessageInfo.getMsg_type().toString());
        Query selectChatRoomDocsQuery = new Query(Criteria.where("_id").is(chatRoomId));
        Query selectChatReadStatusDocs = new Query(Criteria.where("_id").is(ChatUtils.addString(chatRoomId,getSenderEmail)));

        Update updateMessage = new Update().push("messages", chatMessageInfo);

        Update updateChatReadStatus = new Update()
                .set("lastReadMessageId", chatMessageInfo.getMsg())
                .set("lastReadTimestamp",LocalDateTime.now());

        ChatRoomMetadataDocs chatRoomMetadataDocs = updateChatRoomMetaDataDocs(chatMessageInfo, chatRoomId);

        mongoTemplate.updateFirst(selectChatRoomDocsQuery, updateMessage, ChatRoomDocs.class);
        mongoTemplate.updateFirst(selectChatReadStatusDocs, updateChatReadStatus, ChatReadStatusDocs.class);
        mongoTemplate.save(chatRoomMetadataDocs);
    }

    private ChatRoomMetadataDocs updateChatRoomMetaDataDocs(ChatMessageInfo chatMessageInfo, String chatRoomId) {
        return ChatRoomMetadataDocs.updateChatRoomMetaData(chatMessageInfo, chatRoomId);
    }




}
