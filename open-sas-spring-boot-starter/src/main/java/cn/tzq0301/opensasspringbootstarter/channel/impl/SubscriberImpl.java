package cn.tzq0301.opensasspringbootstarter.channel.impl;

import cn.tzq0301.opensasspringbootstarter.callback.SubscriberCallback;
import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.Subscriber;
import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.MessageContent;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SubscriberImpl implements Subscriber {
    private final Group group;
    private final Version version;
    private final Priority priority;
    private final SubscriberCallback callback;

    public SubscriberImpl(@NonNull final Group group,
                          @NonNull final Version version,
                          @NonNull final Priority priority,
                          @NonNull final SubscriberCallback callback) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(callback);
        this.group = group;
        this.version = version;
        this.priority = priority;
        this.callback = callback;
    }

    @Override
    public void onMessage(@NonNull final MessageContent content) {
        checkNotNull(content);
        callback.onMessage(content);
    }

    @Override
    public void subscribe(@NonNull final Channel channel) {
        checkNotNull(channel);
        channel.registerSubscriber(group, version, priority, callback);
    }

    @Override
    public void unsubscribe(@NonNull final Channel channel) {
        checkNotNull(channel);
        channel.unregisterSubscriber(group, version, priority);
    }
}
