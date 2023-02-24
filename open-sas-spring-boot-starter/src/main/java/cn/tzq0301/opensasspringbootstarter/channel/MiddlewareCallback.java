package cn.tzq0301.opensasspringbootstarter.channel;

import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.publish.Publish;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface MiddlewareCallback {
    void onMessage(@NonNull final Message message, @NonNull final Publish publisher);
}
