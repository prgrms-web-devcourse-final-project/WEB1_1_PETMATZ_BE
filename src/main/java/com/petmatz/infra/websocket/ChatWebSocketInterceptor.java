//    package com.petmatz.infra.websocket;
//
//    import com.petmatz.common.exception.InfraException;
//    import com.petmatz.domain.chatting.repository.ChatRoomRepository;
//    import lombok.RequiredArgsConstructor;
//    import org.springframework.http.server.ServerHttpRequest;
//    import org.springframework.http.server.ServerHttpResponse;
//    import org.springframework.stereotype.Component;
//    import org.springframework.web.socket.WebSocketHandler;
//    import org.springframework.web.socket.handler.TextWebSocketHandler;
//    import org.springframework.web.socket.server.HandshakeInterceptor;
//    import org.springframework.web.util.UriComponentsBuilder;
//
//    import java.net.URI;
//    import java.util.List;
//    import java.util.Map;
//
//    import static com.petmatz.common.exception.GlobalErrorCode.PERMISSION_DENIED;
//
//    @Component
//    @RequiredArgsConstructor
//    public class ChatWebSocketInterceptor implements HandshakeInterceptor {
//
//        private final ChatRoomRepository chatRoomRepository;
//
//        @Override
//        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//            URI uri = request.getURI();
//            System.out.println("Incoming URI: " + uri);
//
//            Map<String, List<String>> queryParams = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
//
//            List<String> roomIdList = queryParams.get("roomId");
//            String roomId = (roomIdList != null && !roomIdList.isEmpty()) ? roomIdList.get(0) : null;
//            if (roomId == null || roomId.isEmpty()) {
//                throw new InfraException(PERMISSION_DENIED);
//            }
//            attributes.put("roomId", roomId);
//
//            return true;
//        }
//
//        @Override
//        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
//
//        }
//    }
