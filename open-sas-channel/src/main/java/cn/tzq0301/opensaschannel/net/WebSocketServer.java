//package cn.tzq0301.opensaschannel.net;
//
//import org.checkerframework.checker.nullness.qual.NonNull;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.annotation.SendToUser;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.WebSocketMessage;
//import org.springframework.web.socket.WebSocketSession;
//
//import static com.google.common.base.Preconditions.checkNotNull;
//
//@Component
//public class WebSocketServer implements WebSocketHandler {
//    @Override
//    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
//        checkNotNull(session);
//    }
//
//    @Override
//    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
//        checkNotNull(session);
//        checkNotNull(message);
//        // TODO
//    }
//
//    @Override
//    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
//        checkNotNull(session);
//        checkNotNull(exception);
//        exception.printStackTrace();
//    }
//
//    @Override
//    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
//        checkNotNull(session);
//        checkNotNull(closeStatus);
//    }
//
//    @Override
//    public boolean supportsPartialMessages() {
//        return false;
//    }
//}
