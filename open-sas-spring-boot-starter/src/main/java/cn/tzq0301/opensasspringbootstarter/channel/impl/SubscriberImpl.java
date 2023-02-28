package cn.tzq0301.opensasspringbootstarter.channel.impl;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.Subscriber;
import cn.tzq0301.opensasspringbootstarter.channel.SubscriberCallback;
import cn.tzq0301.opensasspringbootstarter.common.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class SubscriberImpl implements Subscriber {
    private final Group group;

    private final Version version;

    private final Priority priority;

    private final Map<Topic, SubscriberCallback> topicToCallbackMap;

    public SubscriberImpl(@NonNull final Group group,
                          @NonNull final Version version,
                          @NonNull final Priority priority,
                          @NonNull final Map<Topic, SubscriberCallback> topicToCallbackMap) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(topicToCallbackMap);
        this.group = group;
        this.version = version;
        this.priority = priority;
        this.topicToCallbackMap = topicToCallbackMap;
    }

    @Override
    public void onMessage(@NonNull final Topic topic, @NonNull Message message) {
        checkNotNull(message);
        SubscriberCallback callback = checkNotNull(topicToCallbackMap.get(topic));
        callback.onMessage(topic, message);
    }

    @Override
    public void subscribe(@NonNull Channel channel) {
        checkNotNull(channel);
        channel.registerSubscriber(group, version, priority, topicToCallbackMap);
    }

    @Override
    public void unsubscribe(@NonNull Channel channel) {
        checkNotNull(channel);
        channel.unregisterSubscriber(group, version, priority);
    }
}
