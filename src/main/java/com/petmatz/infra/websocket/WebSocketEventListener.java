package com.petmatz.infra.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class WebSocketEventListener {

    // WebSocket 연결 시 사용자 추적
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = accessor.getUser().getName();
        String sessionId = accessor.getSessionId();
        System.out.println("username :: " + username);
        System.out.println("sessionId :: " + sessionId);
    }

}
