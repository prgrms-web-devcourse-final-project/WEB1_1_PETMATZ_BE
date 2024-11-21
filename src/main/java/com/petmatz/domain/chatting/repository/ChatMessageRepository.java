package com.petmatz.domain.chatting.repository;

import com.petmatz.domain.chatting.dto.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
}
