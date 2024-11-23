//package com.petmatz.domain.chatting;
//
//import com.petmatz.domain.chatting.dto.ChatMessage;
//import com.petmatz.domain.chatting.dto.ChatMessageInfo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ChatMessageCahing {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    public void cachingChatMessage(ChatMessageInfo chatMessageInfo) {
//        String chatRoomId = chatMessageInfo.chatRoomId();
//        ChatMessage chatMessage = chatMessageInfo.of();
//
//        redisTemplate.opsForList().rightPush(chatRoomId, chatMessage);
//    }
//}
