package cn.tzq0301.opensascore.subscriber;

import cn.tzq0301.opensascore.channel.Channel;
import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
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
    public void subscribe(@NonNull final Channel channel) {
        checkNotNull(channel);
        channel.registerSubscriber(group, version, priority, topicToCallbackMap);
    }

    @Override
    public void unsubscribe(@NonNull final Channel channel) {
        checkNotNull(channel);
        channel.unregisterSubscriber(group, version, priority);
    }

    @Override
    public void onMessage(@NonNull Group group, @NonNull Version version, @NonNull Priority priority, @NonNull Topic topic, @NonNull Message message) {
        checkNotNull(topic);
        checkNotNull(message);
        SubscriberCallback callback = checkNotNull(topicToCallbackMap.get(topic));
        callback.onMessage(group, version, priority, topic, message);
    }
}
