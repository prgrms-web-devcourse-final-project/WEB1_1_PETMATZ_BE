package com.petmatz.api.chatting.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatMessageResponse(
        //chatRoom Id
        String _id,

        List<ChatMessage> chatMessage,

        int pageNumber,

        int totalPages,

        long totalElements,

        IChatUserResponse other
) {

    public static ChatMessageResponse of(List<ChatMessage> chatMessage,
                                         IChatUserResponse other,
                                         String _id,
                                         int pageNumber,
                                         int totalPages,
                                         long totalElements) {
        return ChatMessageResponse.builder()
                ._id(_id)
                .chatMessage(chatMessage)
                .pageNumber(pageNumber)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .other(other)
                .build();
    }


}
