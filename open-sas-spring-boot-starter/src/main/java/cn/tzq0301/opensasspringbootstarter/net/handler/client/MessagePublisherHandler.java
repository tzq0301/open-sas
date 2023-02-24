package cn.tzq0301.opensasspringbootstarter.net.handler.client;

import cn.tzq0301.opensasspringbootstarter.channel.Publisher;
import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.EndpointRegistry;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.publish.PublishClient;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.publish.PublishRequest;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.registry.EndpointRegistryImpl;
import cn.tzq0301.opensasspringbootstarter.net.common.payload.Payload;
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
public final class MessagePublisherHandler implements WebSocketHandler, Publisher {
    private final EndpointRegistry endpointRegistry;

    private WebSocketSession session; // init after connection established

    private final Publisher publisher;

    public MessagePublisherHandler(@NonNull final Group group,
                                   @NonNull final Version version,
                                   @NonNull final Priority priority) {
        this.endpointRegistry = new EndpointRegistryImpl() {{
            register(new PublishClient());
        }};
        this.publisher = message -> {
            checkNotNull(message);
            endpointRegistry.call(Payload.fromData(group, version, priority, new PublishRequest(message)), session);
        };
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

    @Override
    public void publish(@NonNull final Message message) {
        checkNotNull(message);
        publisher.publish(message);
    }
}
