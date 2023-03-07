package cn.tzq0301.opensascore.middleware;

import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.publisher.Publisher;
import cn.tzq0301.opensascore.topic.Topic;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface MiddlewareCallback {
    void onMessage(@NonNull final Topic topic, @NonNull final Message message, @NonNull final Publisher publisher);
}
