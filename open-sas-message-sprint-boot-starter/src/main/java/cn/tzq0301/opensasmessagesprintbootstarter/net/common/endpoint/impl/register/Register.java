package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.register;

import cn.tzq0301.opensasmessagesprintbootstarter.channel.Channel;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.impl.SubscriberImpl;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.Endpoint;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.payload.Payload;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.request.Request;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static cn.tzq0301.opensasmessagesprintbootstarter.net.common.response.ResponseEnum.ILLEGAL_ARGUMENT;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public record Register(@NonNull Channel channel) implements Endpoint {
    public static final String NAME = "register";

    private static final ObjectMapper mapper = new ObjectMapper(); // no need for each instance & concurrent safety

    public Register {
        checkNotNull(channel);
    }

    @Override
    public Response<?> call(@NonNull Request<?> request, @NonNull WebSocketSession session) {
        checkNotNull(request);
        checkNotNull(session);

        Payload<?> payload = checkNotNull(request.payload());
        String method = checkNotNull(payload.method());
        checkArgument(NAME.equals(method));

        RegisterRequest entity;
        try {
            entity = mapper.readValue(mapper.writeValueAsString(payload.data()), RegisterRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.error(ILLEGAL_ARGUMENT);
        }

        channel.registerSubscriber(new SubscriberImpl(entity.group(), entity.version(), entity.priority(), content -> {
            try {
                session.sendMessage(new TextMessage(mapper.writeValueAsString(content)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        return Response.success(new Payload<>(method, null));
    }
}
