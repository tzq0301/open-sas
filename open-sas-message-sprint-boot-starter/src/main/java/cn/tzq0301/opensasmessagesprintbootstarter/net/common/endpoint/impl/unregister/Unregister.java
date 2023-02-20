package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.unregister;

import cn.tzq0301.opensasmessagesprintbootstarter.channel.Channel;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.impl.SubscriberImpl;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.Endpoint;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.register.RegisterRequest;
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

public record Unregister(@NonNull Channel channel) implements Endpoint {
    public static final String NAME = "unregister";

    private static final ObjectMapper mapper = new ObjectMapper(); // no need for each instance & concurrent safety

    public Unregister {
        checkNotNull(channel);
    }

    @Override
    public Response<?> call(@NonNull Request<?> request, @NonNull WebSocketSession session) {
        checkNotNull(request);
        checkNotNull(session);

        Payload<?> payload = checkNotNull(request.payload());
        String method = checkNotNull(payload.method());
        checkArgument(NAME.equals(method));

        UnregisterRequest entity;
        try {
            entity = mapper.readValue(mapper.writeValueAsString(payload.data()), UnregisterRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.error(ILLEGAL_ARGUMENT);
        }

        channel.unregisterSubscriber(new SubscriberImpl(entity.group(), entity.version(), entity.priority(), content -> {}));

        return Response.success(new Payload<>(method, null));
    }
}
