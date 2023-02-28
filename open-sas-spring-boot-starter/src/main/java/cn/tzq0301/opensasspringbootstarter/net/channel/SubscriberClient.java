package cn.tzq0301.opensasspringbootstarter.net.channel;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.Subscriber;
import cn.tzq0301.opensasspringbootstarter.channel.SubscriberCallback;
import cn.tzq0301.opensasspringbootstarter.common.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SubscriberClient implements Subscriber {
    private final Group group;

    private final Version version;

    private final Priority priority;

    private final Map<Topic, SubscriberCallback> topicToCallbackMap;

    public SubscriberClient(@NonNull final Group group,
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
    public void subscribe(@NonNull Channel channel) {
        channel.registerSubscriber(group, version, priority, topicToCallbackMap);
    }

    @Override
    public void unsubscribe(@NonNull Channel channel) {
        channel.unregisterSubscriber(group, version, priority);
    }

    @Override
    public void onMessage(@NonNull final Topic topic, @NonNull final Message message) {
        checkNotNull(topicToCallbackMap.get(topic)).onMessage(topic, message);
    }
}
