package cn.tzq0301.opensasspringbootstarter.net.channel;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.Subscriber;
import cn.tzq0301.opensasspringbootstarter.channel.SubscriberCallback;
import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SubscriberClient implements Subscriber {
    private final Group group;

    private final Version version;

    private final Priority priority;

    private final List<SubscriberCallback> callbacks;

    public SubscriberClient(@NonNull final Group group,
                            @NonNull final Version version,
                            @NonNull final Priority priority,
                            @NonNull final List<SubscriberCallback> callbacks) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(callbacks);
        this.group = group;
        this.version = version;
        this.priority = priority;
        this.callbacks = callbacks;
    }

    @Override
    public void subscribe(@NonNull Channel channel) {
        callbacks.forEach(callback -> channel.registerSubscriber(group, version, priority, callback));
    }

    @Override
    public void unsubscribe(@NonNull Channel channel) {
        callbacks.forEach(callback -> channel.unregisterSubscriber(group, version, priority));
    }

    @Override
    public void onMessage(@NonNull Message message) {
        callbacks.forEach(callback -> callback.onMessage(message));
    }
}
