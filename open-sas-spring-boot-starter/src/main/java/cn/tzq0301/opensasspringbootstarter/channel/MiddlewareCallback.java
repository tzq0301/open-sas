package cn.tzq0301.opensasspringbootstarter.channel;

import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Topic;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface MiddlewareCallback {
    void onMessage(@NonNull final Topic topic, @NonNull final Message message, @NonNull final Publisher publisher);
}
