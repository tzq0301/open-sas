package cn.tzq0301.opensasspringbootstarter.channel;

import cn.tzq0301.opensasspringbootstarter.callback.SubscriberCallback;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Subscriber extends SubscriberCallback {
    void subscribe(@NonNull final Channel channel);

    void unsubscribe(@NonNull final Channel channel);
}
