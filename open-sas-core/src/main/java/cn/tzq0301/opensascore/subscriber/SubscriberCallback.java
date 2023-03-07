package cn.tzq0301.opensascore.subscriber;

import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.topic.Topic;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface SubscriberCallback {
    void onMessage(@NonNull Topic topic, @NonNull Message message);
}
