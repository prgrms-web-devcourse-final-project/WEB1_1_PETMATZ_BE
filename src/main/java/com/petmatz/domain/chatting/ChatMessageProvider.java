package com.petmatz.domain.chatting;

import com.petmatz.common.exception.DomainException;
import com.petmatz.domain.chatting.dto.*;


import com.petmatz.domain.chatting.repository.ChatRoomRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Optional;

import static com.petmatz.common.exception.GlobalErrorCode.PERMISSION_DENIED;


@Component
@RequiredArgsConstructor
public class ChatMessageProvider {

    private final MongoTemplate mongoTemplate;
    private final ChatRoomRepository chatRoomRepository;

    //메세지 추가하는 로직 ( 메세지는 insert, 메타 데이터는 new )
    public void append(ChatMessageInfo chatMessageInfo) {
        ChatMessage chatMessage = chatMessageInfo.of();

        Query id = new Query(Criteria.where("_id").is(chatMessageInfo.chatRoomId()));
        Update updateMessage = new Update().push("messages", chatMessage);

        ChatRoomMetadataDocs chatRoomMetadataDocs = updateChatRoomMetaDataDocs(chatMessageInfo);


        mongoTemplate.updateFirst(id, updateMessage, ChatRoomDocs.class);
        mongoTemplate.save(chatRoomMetadataDocs);
    }


    //채팅방 생성시 이에 해당하는 Docs도 같이 생성 ( 채팅 내역, 채팅방 메타 데이터 )
    public void init(ChatRoomInfo chatRoomInfo,long chatRoomID) {
        ChatRoomDocs chatRoomDocs = createdChatRooms(chatRoomInfo, chatRoomID);
        ChatRoomMetadataDocs chatRoomMetadataDocs = createdChatRoomMetaDataDocs(chatRoomInfo, chatRoomID);



        mongoTemplate.save(chatRoomDocs);
        mongoTemplate.save(chatRoomMetadataDocs);
    }

    private ChatRoomMetadataDocs updateChatRoomMetaDataDocs(ChatMessageInfo chatMessageInfo) {
        return ChatRoomMetadataDocs.updateChatRoomMetaData(chatMessageInfo);
    }

    private ChatRoomMetadataDocs createdChatRoomMetaDataDocs(ChatRoomInfo chatRoomInfo,long chatRoomID) {
        return ChatRoomMetadataDocs.initChatRoomMetaData(chatRoomInfo,chatRoomID);
    }

    private ChatRoomDocs createdChatRooms(ChatRoomInfo chatRoomInfo,long chatRoomID) {
        return ChatRoomDocs.initChatRoomDocs(chatRoomInfo, chatRoomID );
    }

}
