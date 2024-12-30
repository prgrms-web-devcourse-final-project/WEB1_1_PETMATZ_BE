package com.petmatz.domain.petmission.exception;

import com.petmatz.common.exception.DomainException;

public class NotFoundChatRoomId extends DomainException {
    public static final DomainException EXCEPTION = new NotFoundChatRoomId();

    public NotFoundChatRoomId() {
        super(ChatRoomErrorCode.NOT_FOUND_CHATROOM);
    }
}
