package com.petmatz.domain.chatting.repository;

import com.petmatz.domain.chatting.dto.ChatRoomMetadataDocs;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomMetadataRepository  extends MongoRepository<ChatRoomMetadataDocs, String> {
}
