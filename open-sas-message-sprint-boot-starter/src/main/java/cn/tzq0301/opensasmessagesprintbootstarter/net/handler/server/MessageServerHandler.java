package cn.tzq0301.opensasmessagesprintbootstarter.net.handler.server;

import cn.tzq0301.opensasmessagesprintbootstarter.channel.Channel;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.impl.ChannelImpl;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.EndpointRegistry;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.EndpointRegistryImpl;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.register.Register;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.unregister.Unregister;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.request.Request;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.Map;

import static cn.tzq0301.opensasmessagesprintbootstarter.net.common.response.ResponseEnum.ILLEGAL_ARGUMENT;
import static cn.tzq0301.opensasmessagesprintbootstarter.net.common.response.ResponseEnum.SERVER_ERROR;
import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Import({ChannelImpl.class})
@ConditionalOnProperty(prefix = "open-sas", name = "server", havingValue = "true")
public final class MessageServerHandler implements WebSocketHandler {
    private final EndpointRegistry endpointRegistry;

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public MessageServerHandler(@NonNull Channel channel) {
        checkNotNull(channel);
        this.endpointRegistry = new EndpointRegistryImpl(Map.ofEntries(
                Map.entry(Register.NAME, new Register(channel)),
                Map.entry(Unregister.NAME, new Unregister(channel))
        ));
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        checkNotNull(session);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        checkNotNull(session);
        checkNotNull(message);

        Response<?> response;
        Object payload = message.getPayload();

        try {
            Request<?> request = jsonMapper.readValue(payload.toString(), Request.class);
            response = endpointRegistry.call(request, session);
        } catch (JsonProcessingException e) {
            response = Response.error(ILLEGAL_ARGUMENT, payload);
        }

        String value = jsonMapper.writeValueAsString(response);
        session.sendMessage(new TextMessage(value));
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        checkNotNull(session);
        checkNotNull(exception);
        exception.printStackTrace();
        session.sendMessage(new TextMessage(jsonMapper.writeValueAsString(Response.error(SERVER_ERROR, exception.getMessage()))));
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
