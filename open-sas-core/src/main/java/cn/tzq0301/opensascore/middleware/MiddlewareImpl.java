package cn.tzq0301.opensascore.middleware;

import cn.tzq0301.opensascore.channel.Channel;
import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.publisher.Publisher;
import cn.tzq0301.opensascore.subscriber.Subscriber;
import cn.tzq0301.opensascore.subscriber.SubscriberImpl;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public final class MiddlewareImpl implements Middleware {
    private final Publisher publisher;

    private final Subscriber subscriber;

    public MiddlewareImpl(@NonNull final Channel channel,
                          @NonNull final Group group,
                          @NonNull final Version version,
                          @NonNull final Priority priority,
                          @NonNull final Map<Topic, MiddlewareCallback> topicToCallbackMap) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(topicToCallbackMap);
        this.publisher = (topic, message) -> {
            checkNotNull(topic);
            checkNotNull(message);
            channel.publish(group, version, priority.cloneByDownGrade(), topic, message);
        };
        this.subscriber = new SubscriberImpl(group, version, priority, new HashMap<>() {{
            topicToCallbackMap.forEach((topic, callback) -> put(topic, (t, message) -> {
                checkNotNull(t);
                checkNotNull(message);
                callback.onMessage(t, message, publisher);
            }));
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
