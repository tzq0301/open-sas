package cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.register;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.SubscriberCallback;
import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.Endpoint;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.WebSocketEndpoint;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.publish.PublishRequest;
import cn.tzq0301.opensasspringbootstarter.net.common.payload.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.stream.Collectors;

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

        System.out.println(payload); // FIXME

        Group group = payload.group();
        Version version = payload.version();
        Priority priority = payload.priority();
        RegisterRequest request = mapper.convertValue(payload.data(), RegisterRequest.class);

        SubscriberCallback callback = (topic, message) -> {
            Payload publishPayload = Payload.fromData(group, version, priority, new PublishRequest(topic, message));
            try {
                String text = mapper.writeValueAsString(publishPayload);
                System.out.println(text); // FIXME
                TextMessage textMessage = new TextMessage(text);
                session.sendMessage(textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        channel.registerSubscriber(group, version, priority, request.topics().stream()
                .collect(Collectors.toMap(topic -> topic, topic -> callback)));
    }
}
