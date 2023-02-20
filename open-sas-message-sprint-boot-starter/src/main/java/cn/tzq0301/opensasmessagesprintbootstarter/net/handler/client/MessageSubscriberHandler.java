package cn.tzq0301.opensasmessagesprintbootstarter.net.handler.client;

import cn.tzq0301.opensasmessagesprintbootstarter.common.Group;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Priority;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Version;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.register.Register;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.register.RegisterRequest;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.unregister.Unregister;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.unregister.UnregisterRequest;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.payload.Payload;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.request.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@ConditionalOnProperty(prefix = "open-sas.subscriber", name = "enable", havingValue = "true")
public final class MessageSubscriberHandler implements WebSocketHandler {
    private final ObjectMapper objectMapper;

    private WebSocketSession session;

    private final Group group;

    private final Version version;

    private final Priority priority;

    public MessageSubscriberHandler(Group group, Version version, Priority priority) {
        this.objectMapper = new ObjectMapper();
        this.group = group;
        this.version = version;
        this.priority = priority;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        checkNotNull(session);
        this.session = session;

        Payload<RegisterRequest> payload = new Payload<>(Register.NAME, new RegisterRequest(group, version, priority));
        Request<RegisterRequest> request = new Request<>(payload);
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(request));
        session.sendMessage(textMessage);
    }

    public void shutdown() throws IOException {
        Payload<UnregisterRequest> payload = new Payload<>(Unregister.NAME, new UnregisterRequest(group, version, priority));
        Request<UnregisterRequest> request = new Request<>(payload);
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(request));
        session.sendMessage(textMessage);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        checkNotNull(session);
        checkNotNull(message);
        // TODO
        System.out.println(message);
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        checkNotNull(session);
        checkNotNull(exception);
        exception.printStackTrace();
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        checkNotNull(session);
        checkNotNull(closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
