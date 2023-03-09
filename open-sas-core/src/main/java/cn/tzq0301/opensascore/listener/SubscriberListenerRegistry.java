package cn.tzq0301.opensascore.listener;

import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.subscriber.SubscriberCallback;
import cn.tzq0301.opensascore.topic.Topic;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class SubscriberListenerRegistry implements SubscriberCallback {
    private final Map<Topic, SubscriberCallback> topicToCallbackMap;

    public SubscriberListenerRegistry() {
        this.topicToCallbackMap = Maps.newHashMap();
    }

    public void add(@NonNull final Topic topic, @NonNull final SubscriberCallback callback) {
        checkNotNull(topic);
        checkNotNull(callback);
        checkArgument(topicToCallbackMap.get(topic) == null, "duplicate listener for same topic (%s)", topic);
        topicToCallbackMap.put(topic, callback);
    }

    public Set<Topic> getTopics() {
        return ImmutableSet.copyOf(topicToCallbackMap.keySet());
    }

    @Override
    public void onMessage(@NonNull Topic topic, @NonNull Message message) {
        checkNotNull(topic);
        checkNotNull(message);
        checkNotNull(topicToCallbackMap.get(topic)).onMessage(topic, message);
    }
}
