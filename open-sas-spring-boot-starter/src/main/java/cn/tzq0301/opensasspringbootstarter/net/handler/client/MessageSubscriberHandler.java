package cn.tzq0301.opensasspringbootstarter.net.handler.client;

import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.EndpointRegistry;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.register.RegisterClient;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.register.RegisterRequest;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.registry.EndpointRegistryImpl;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.unregister.UnregisterClient;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.unregister.UnregisterRequest;
import cn.tzq0301.opensasspringbootstarter.net.common.payload.Payload;
import cn.tzq0301.opensasspringbootstarter.sdk.subscriber.OnMessageCallbackRegister;
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

    private final EndpointRegistry endpointRegistry;

    private WebSocketSession session; // init after connection established

    private final Group group;

    private final Version version;

    private final Priority priority;

//    private final OnMessageCallback callback;

    private final OnMessageCallbackRegister register;

    public MessageSubscriberHandler(@NonNull final Group group,
                                    @NonNull final Version version,
                                    @NonNull final Priority priority,
//                                    @NonNull final OnMessageCallback callback,
                                    @NonNull final OnMessageCallbackRegister register) {
        this.group = group;
        this.version = version;
        this.priority = priority;
//        this.callback = callback;
        this.register = register;
        this.endpointRegistry = new EndpointRegistryImpl() {{
            register(new RegisterClient());
            register(new UnregisterClient());
        }};
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        checkNotNull(session);
        this.session = session;

        register.forEach(callback -> {
            Payload payload = Payload.fromData(group, version, priority, new RegisterRequest());
            endpointRegistry.call(payload, session);
        });

        // TODO not register here
//        Payload payload = Payload.fromData(group, version, priority, new RegisterRequest());
//        endpointRegistry.call(payload, session);
    }

    public void shutdown() throws IOException {
        endpointRegistry.call(Payload.fromData(group, version, priority, new UnregisterRequest()), session);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        checkNotNull(session);
        checkNotNull(message);

        Message messageFromServer = mapper.readValue(message.getPayload().toString(), Message.class);
        // TODO
//        callback.onMessage(messageFromServer);
        register.forEach(callback -> callback.onMessage(messageFromServer));
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
