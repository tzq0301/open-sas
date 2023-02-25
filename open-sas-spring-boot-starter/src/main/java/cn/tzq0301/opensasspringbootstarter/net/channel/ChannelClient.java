package cn.tzq0301.opensasspringbootstarter.net.channel;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.SubscriberCallback;
import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
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
    public void registerSubscriber(@NonNull Group group, @NonNull Version version, @NonNull Priority priority, @NonNull SubscriberCallback subscriber) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(subscriber);
        Payload registerPayload = Payload.fromData(group, version, priority, new RegisterRequest());
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
    public void publish(@NonNull Group group, @NonNull Version version, @NonNull Priority priority, @NonNull Message message) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(message);
        Payload publishPayload = Payload.fromData(group, version, priority, new PublishRequest(message));
        endpointRegistry.call(publishPayload, session);
    }
}
