package cn.tzq0301.opensasspringbootstarter.net.channel;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.SubscriberCallback;
import cn.tzq0301.opensasspringbootstarter.common.*;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.EndpointRegistry;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.publish.PublishClient;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.publish.PublishRequest;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.register.RegisterClient;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.register.RegisterRequest;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.registry.EndpointRegistryImpl;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.unregister.UnregisterClient;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.unregister.UnregisterRequest;
import cn.tzq0301.opensasspringbootstarter.net.common.payload.Payload;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChannelClient implements Channel {
    private final EndpointRegistry endpointRegistry;

    private final WebSocketSession session;

    public ChannelClient(@NonNull final WebSocketSession session) {
        this.endpointRegistry = new EndpointRegistryImpl() {{
            register(new RegisterClient());
            register(new UnregisterClient());
            register(new PublishClient());
        }};
        this.session = session;
    }

    @Override
    public void registerSubscriber(@NonNull final Group group,
                                   @NonNull final Version version,
                                   @NonNull final Priority priority,
                                   @NonNull final Map<Topic, SubscriberCallback> topicToCallbackMap) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(topicToCallbackMap);
        Payload registerPayload = Payload.fromData(group, version, priority, new RegisterRequest(topicToCallbackMap.keySet()));
        endpointRegistry.call(registerPayload, session);
    }

    @Override
    public void unregisterSubscriber(@NonNull Group group, @NonNull Version version, @NonNull Priority priority) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        Payload unregisterPayload = Payload.fromData(group, version, priority, new UnregisterRequest());
        endpointRegistry.call(unregisterPayload, session);
    }

    @Override
    public void publish(@NonNull final Group group,
                        @NonNull final Version version,
                        @NonNull final Priority priority,
                        @NonNull final Topic topic,
                        @NonNull final Message message) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(message);
        PublishRequest publishRequest = new PublishRequest(topic, message);
        Payload publishPayload = Payload.fromData(group, version, priority, publishRequest);
        endpointRegistry.call(publishPayload, session);
    }
}
