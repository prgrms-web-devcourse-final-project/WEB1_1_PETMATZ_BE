package com.petmatz.infra.websocket.exception;

import com.petmatz.common.exception.BaseErrorCode;
import com.petmatz.common.exception.InfraException;

public class SubscriptionUrlNotFountException extends InfraException {

    public static final InfraException EXCEPTION = new SubscriptionUrlNotFountException();

    private SubscriptionUrlNotFountException() {
        super(WebSocketErrorCode.SUBSCRIPTION_URL_NOT_FOUNT);
    }
}
