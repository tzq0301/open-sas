package cn.tzq0301.opensasmessagesprintbootstarter.net.handler.server;

import cn.tzq0301.opensasmessagesprintbootstarter.channel.Channel;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.impl.ChannelImpl;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.EndpointRegistry;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.registry.EndpointRegistryImpl;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.register.Register;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.unregister.Unregister;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.payload.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Import({ChannelImpl.class})
@ConditionalOnProperty(prefix = "open-sas", name = "server", havingValue = "true")
public final class MessageServerHandler implements WebSocketHandler {
    private final EndpointRegistry endpointRegistry;

    private static final ObjectMapper mapper = new ObjectMapper();

    public MessageServerHandler(@NonNull Channel channel) {
        checkNotNull(channel);
        this.endpointRegistry = new EndpointRegistryImpl() {{
            register(new Register(channel));
            register(new Unregister(channel));
        }};
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        checkNotNull(session);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        checkNotNull(session);
        checkNotNull(message);

        Payload payload = mapper.readValue(message.getPayload().toString(), Payload.class);
        endpointRegistry.call(payload, session);
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        checkNotNull(session);
        checkNotNull(exception);
        exception.printStackTrace();
        session.sendMessage(new TextMessage(mapper.writeValueAsString(exception.getMessage())));
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
