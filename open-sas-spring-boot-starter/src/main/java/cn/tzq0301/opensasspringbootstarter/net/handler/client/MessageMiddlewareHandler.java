package cn.tzq0301.opensasspringbootstarter.net.handler.client;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.Middleware;
import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
import cn.tzq0301.opensasspringbootstarter.net.channel.ChannelClient;
import cn.tzq0301.opensasspringbootstarter.net.channel.MiddlewareClient;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.publish.PublishRequest;
import cn.tzq0301.opensasspringbootstarter.net.common.payload.Payload;
import cn.tzq0301.opensasspringbootstarter.sdk.middleware.MiddlewareCallbackRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@ConditionalOnProperty(prefix = "open-sas.middleware", name = "enable", havingValue = "true")
public final class MessageMiddlewareHandler implements WebSocketHandler {
    private static final ObjectMapper mapper = new ObjectMapper();

    private final Group group;

    private final Version version;

    private final Priority priority;

    private final MiddlewareCallbackRegistry callbackRegistry;

    private Channel channel; // init after connection established

    private Middleware middleware; // init after connection established

    public MessageMiddlewareHandler(@NonNull final Group group,
                                    @NonNull final Version version,
                                    @NonNull final Priority priority,
                                    @NonNull final MiddlewareCallbackRegistry callbackRegistry) {
        this.group = group;
        this.version = version;
        this.priority = priority;
        this.callbackRegistry = callbackRegistry;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        checkNotNull(session);

        channel = new ChannelClient(session);

        middleware = new MiddlewareClient(channel, group, version, priority, callbackRegistry.getCallbacks());
        middleware.subscribe(channel);
    }

    public void shutdown() throws IOException {
        checkNotNull(channel);
        checkNotNull(middleware);
        middleware.unsubscribe(channel);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        checkNotNull(session);
        checkNotNull(message);

        Payload payloadFromServer = mapper.readValue(message.getPayload().toString(), Payload.class);
        PublishRequest publishRequest = mapper.convertValue(payloadFromServer.data(), PublishRequest.class);
        callbackRegistry.onMessage(publishRequest.topic(), publishRequest.message(), middleware);
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
