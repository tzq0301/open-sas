package cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.unregister;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.Endpoint;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.WebSocketEndpoint;
import cn.tzq0301.opensasspringbootstarter.net.common.payload.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.socket.WebSocketSession;

import static com.google.common.base.Preconditions.checkNotNull;

@WebSocketEndpoint("Unregister")
public final class Unregister implements Endpoint {
    private static final ObjectMapper mapper = new ObjectMapper(); // no need for each instance & concurrent safety

    private final Channel channel;

    public Unregister(@NonNull Channel channel) {
        checkNotNull(channel);
        this.channel = channel;
    }

    @Override
    public void call(@Nullable Payload payload, @NonNull WebSocketSession session) {
        checkNotNull(payload);
        checkNotNull(session);

        channel.unregisterSubscriber(payload.group(), payload.version(), payload.priority());
    }

}
