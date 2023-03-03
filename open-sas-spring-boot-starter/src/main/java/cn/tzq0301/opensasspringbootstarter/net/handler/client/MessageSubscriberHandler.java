package cn.tzq0301.opensasspringbootstarter.net.handler.client;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.Subscriber;
import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
import cn.tzq0301.opensasspringbootstarter.net.channel.ChannelClient;
import cn.tzq0301.opensasspringbootstarter.net.channel.SubscriberClient;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.publish.PublishRequest;
import cn.tzq0301.opensasspringbootstarter.net.common.payload.Payload;
import cn.tzq0301.opensasspringbootstarter.sdk.subscriber.SubscriberCallbackRegistry;
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
@ConditionalOnProperty(prefix = "open-sas.subscriber", name = "enable", havingValue = "true")
public final class MessageSubscriberHandler implements WebSocketHandler {
    private static final ObjectMapper mapper = new ObjectMapper();

    private final Group group;

    private final Version version;

    private final Priority priority;

    private final SubscriberCallbackRegistry callbackRegistry;

    private Channel channel; // init after connection established

    private Subscriber subscriber; // init after connection established

    public MessageSubscriberHandler(@NonNull final Group group,
                                    @NonNull final Version version,
                                    @NonNull final Priority priority,
                                    @NonNull final SubscriberCallbackRegistry callbackRegistry) {
        this.group = group;
        this.version = version;
        this.priority = priority;
        this.callbackRegistry = callbackRegistry;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        checkNotNull(session);

        channel = new ChannelClient(session);

        subscriber = new SubscriberClient(group, version, priority, callbackRegistry.getCallbacks());
        subscriber.subscribe(channel);
    }

    public void shutdown() throws IOException {
        checkNotNull(channel);
        checkNotNull(subscriber);
        subscriber.unsubscribe(channel);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        checkNotNull(session);
        checkNotNull(message);

        Payload payloadFromServer = mapper.readValue(message.getPayload().toString(), Payload.class);
        PublishRequest publishRequest = mapper.convertValue(payloadFromServer.data(), PublishRequest.class);
        callbackRegistry.onMessage(publishRequest.topic(), publishRequest.message());
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
