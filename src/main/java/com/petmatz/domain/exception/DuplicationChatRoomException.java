package com.petmatz.domain.exception;

import com.petmatz.common.exception.DomainException;

public class DuplicationChatRoomException extends DomainException {
    public static final DomainException EXCEPTION = new DuplicationChatRoomException();

    private DuplicationChatRoomException() {
        super(ChatErrorCode.CHAT_ROOM_DUPLICATION);
    }
}
