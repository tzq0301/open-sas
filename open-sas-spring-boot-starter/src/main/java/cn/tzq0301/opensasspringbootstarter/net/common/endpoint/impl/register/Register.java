package cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.register;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.Endpoint;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.WebSocketEndpoint;
import cn.tzq0301.opensasspringbootstarter.net.common.payload.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

@WebSocketEndpoint("Register")
public final class Register implements Endpoint {
    private static final ObjectMapper mapper = new ObjectMapper(); // no need for each instance & concurrent safety

    private final Channel channel;

    public Register(@NonNull final Channel channel) {
        checkNotNull(channel);
        this.channel = channel;
    }

    @Override
    public void call(@Nullable final Payload payload, @NonNull final WebSocketSession session) {
        checkNotNull(payload);
        checkNotNull(session);

        RegisterRequest request = mapper.convertValue(payload.data(), RegisterRequest.class);
        channel.registerSubscriber(request.group(), request.version(), request.priority(), content -> {
            try {
                Message message = new Message(request.group(), request.version(), request.priority(), content);
                TextMessage textMessage = new TextMessage(mapper.writeValueAsString(message));
                session.sendMessage(textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
