package cn.tzq0301.opensascore.channel;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.message.MessageDetails;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.subscriber.SubscriberCallback;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

public interface Channel {
    void registerSubscriber(@NonNull Group group, @NonNull Version version, @NonNull Priority priority,
                            @NonNull Map<Topic, SubscriberCallback> topicToCallbackMap);

    void unregisterSubscriber(@NonNull Group group, @NonNull Version version, @NonNull Priority priority);

    void publish(@NonNull Group group, @NonNull Version version, @NonNull Priority priority,
                 @NonNull Topic topic, @NonNull Message message);

    default void publish(@NonNull MessageDetails details) {
        publish(details.group(), details.version(), details.priority(), details.topic(), details.message());
    }
}
