package cn.tzq0301.opensasspringbootstarter.channel;

import cn.tzq0301.opensasspringbootstarter.common.Message;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface MiddlewareCallback {
    void onMessage(@NonNull final Message message, @NonNull final Publisher publisher);
}
