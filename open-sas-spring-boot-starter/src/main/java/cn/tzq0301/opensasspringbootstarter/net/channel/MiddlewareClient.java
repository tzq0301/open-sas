package cn.tzq0301.opensasspringbootstarter.net.channel;

import cn.tzq0301.opensasspringbootstarter.channel.*;
import cn.tzq0301.opensasspringbootstarter.channel.impl.SubscriberImpl;
import cn.tzq0301.opensasspringbootstarter.common.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public final class MiddlewareClient implements Middleware {
    private final Publisher publisher;

    private final Subscriber subscriber;

    public MiddlewareClient(@NonNull final Channel channel,
                            @NonNull final Group group,
                            @NonNull final Version version,
                            @NonNull final Priority priority,
                            @NonNull final Map<Topic, MiddlewareCallback> topicToCallbackMap) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(topicToCallbackMap);
        this.publisher = (topic, message) -> channel.publish(group, version, Priorities.cloneByDownGrade(priority), topic, message);
        this.subscriber = new SubscriberImpl(group, version, priority, new HashMap<>() {{
            topicToCallbackMap.forEach((topic, callback) -> put(topic, (t, message) -> callback.onMessage(t, message, publisher)));
        }});
    }

    @Override
    public void publish(@NonNull Topic topic, @NonNull Message message) {
        checkNotNull(topic);
        checkNotNull(message);
        publisher.publish(topic, message);
    }

    @Override
    public void subscribe(@NonNull Channel channel) {
        checkNotNull(channel);
        subscriber.subscribe(channel);
    }

    @Override
    public void unsubscribe(@NonNull Channel channel) {
        checkNotNull(channel);
        subscriber.unsubscribe(channel);
    }

    @Override
    public void onMessage(@NonNull Topic topic, @NonNull Message message) {
        checkNotNull(topic);
        checkNotNull(message);
        subscriber.onMessage(topic, message);
    }
}
