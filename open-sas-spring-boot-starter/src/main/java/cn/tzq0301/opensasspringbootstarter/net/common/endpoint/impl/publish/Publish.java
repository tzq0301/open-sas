package cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.publish;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.impl.PublisherImpl;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Topic;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.Endpoint;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.WebSocketEndpoint;
import cn.tzq0301.opensasspringbootstarter.net.common.payload.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.socket.WebSocketSession;

import static com.google.common.base.Preconditions.checkNotNull;

@WebSocketEndpoint("Publish")
public final class Publish implements Endpoint {
    private final Channel channel;

    private static final ObjectMapper mapper = new ObjectMapper(); // no need for each instance & concurrent safety

    public Publish(@NonNull final Channel channel) {
        checkNotNull(channel);
        this.channel = channel;
    }

    @Override
    public void call(@Nullable Payload payload, @NonNull WebSocketSession session) {
        checkNotNull(payload);
        checkNotNull(session);

        PublishRequest request = mapper.convertValue(payload.data(), PublishRequest.class);

        System.out.println(request); // FIXME

        Topic topic = request.topic();
        Message message = request.message();
        new PublisherImpl(channel, payload.group(), payload.version(), payload.priority()).publish(topic, message);
    }
}
