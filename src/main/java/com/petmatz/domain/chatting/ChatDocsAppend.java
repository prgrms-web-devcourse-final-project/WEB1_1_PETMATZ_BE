package com.petmatz.domain.chatting;

import com.petmatz.domain.chatting.dto.*;


import com.petmatz.domain.chatting.repository.ChatRoomRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class ChatDocsAppend {

    private final MongoTemplate mongoTemplate;
    private final ChatRoomRepository chatRoomRepository;




    //채팅방 생성시 이에 해당하는 Docs도 같이 생성 ( 채팅 내역, 채팅방 메타 데이터 )
    public void init(ChatRoomInfo chatRoomInfo,long chatRoomID) {
        ChatRoomDocs chatRoomDocs = createdChatRooms(chatRoomInfo, chatRoomID);
        ChatRoomMetadataDocs chatRoomMetadataDocs = createdChatRoomMetaDataDocs(chatRoomInfo, chatRoomID);
        ChatReadStatusDocs caregiverChatReadStatusDocs = createdChatReadStatusDocs(chatRoomInfo.caregiverInfo(),chatRoomID);
        ChatReadStatusDocs entrustedChatReadStatusDocs = createdChatReadStatusDocs(chatRoomInfo.entrustedInfo(),chatRoomID);


        mongoTemplate.save(caregiverChatReadStatusDocs);
        mongoTemplate.save(entrustedChatReadStatusDocs);
        mongoTemplate.save(chatRoomDocs);
        mongoTemplate.save(chatRoomMetadataDocs);
    }




    private ChatReadStatusDocs createdChatReadStatusDocs(String userNmae,long chatRoomID) {
        return ChatReadStatusDocs.initChatReadStatusData(userNmae, chatRoomID);
    }

    private ChatRoomMetadataDocs createdChatRoomMetaDataDocs(ChatRoomInfo chatRoomInfo,long chatRoomID) {
        return ChatRoomMetadataDocs.initChatRoomMetaData(chatRoomInfo,chatRoomID);
    }

    private ChatRoomDocs createdChatRooms(ChatRoomInfo chatRoomInfo,long chatRoomID) {
        return ChatRoomDocs.initChatRoomDocs(chatRoomInfo, chatRoomID );
    }




}
