package cn.tzq0301.opensasmessagesprintbootstarter.net.handler.client;

import cn.tzq0301.opensasmessagesprintbootstarter.common.*;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.EndpointRegistry;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.publish.PublishClient;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.publish.PublishRequest;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.registry.EndpointRegistryImpl;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.payload.Payload;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@ConditionalOnProperty(prefix = "open-sas.publisher", name = "enable", havingValue = "true")
public final class MessagePublisherHandler implements WebSocketHandler {
    private final EndpointRegistry endpointRegistry;

    private WebSocketSession session; // init after connection established

    private final Group group;

    private final Version version;

    private final Priority priority;

    public MessagePublisherHandler(@NonNull final Group group,
                                   @NonNull final Version version,
                                   @NonNull final Priority priority) {
        this.group = group;
        this.version = version;
        this.priority = priority;
        this.endpointRegistry = new EndpointRegistryImpl() {{
            register(new PublishClient());
        }};
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        checkNotNull(session);
        this.session = session;
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        checkNotNull(session);
        checkNotNull(message);
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

    public void publish(@NonNull final MessageContent content) {
        checkNotNull(content);
        var message = new Message(group, version, priority, content);
        endpointRegistry.call(Payload.fromData(new PublishRequest(message)), session);
    }
}
