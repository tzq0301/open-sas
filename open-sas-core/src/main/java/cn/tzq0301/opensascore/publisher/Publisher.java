package cn.tzq0301.opensascore.publisher;

import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.topic.Topic;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface Publisher {
    void publish(@NonNull Topic topic, @NonNull Message message);
}
