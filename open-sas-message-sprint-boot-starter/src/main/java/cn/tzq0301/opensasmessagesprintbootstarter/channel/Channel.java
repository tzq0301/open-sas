package cn.tzq0301.opensasmessagesprintbootstarter.channel;

import cn.tzq0301.opensasmessagesprintbootstarter.common.Message;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Channel {
    void registerSubscriber(@NonNull final Subscriber subscriber);

    void unregisterSubscriber(@NonNull final Subscriber subscriber);

    void publish(@NonNull final Message message);
}
