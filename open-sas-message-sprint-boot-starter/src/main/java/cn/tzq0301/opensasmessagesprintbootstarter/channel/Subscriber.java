package cn.tzq0301.opensasmessagesprintbootstarter.channel;

import cn.tzq0301.opensasmessagesprintbootstarter.callback.SubscriberCallback;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Subscriber extends SubscriberCallback {
    void subscribe(@NonNull final Channel channel);

    void unsubscribe(@NonNull final Channel channel);
}
