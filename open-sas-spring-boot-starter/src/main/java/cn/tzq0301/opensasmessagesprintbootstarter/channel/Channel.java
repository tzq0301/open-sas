package cn.tzq0301.opensasmessagesprintbootstarter.channel;

import cn.tzq0301.opensasmessagesprintbootstarter.callback.SubscriberCallback;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Group;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Message;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Priority;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Channel {
    void registerSubscriber(@NonNull final Group group,
                            @NonNull final Version version,
                            @NonNull final Priority priority,
                            @NonNull final SubscriberCallback callback);

    void unregisterSubscriber(@NonNull final Group group,
                              @NonNull final Version version,
                              @NonNull final Priority priority);

    void publish(@NonNull final Message message);
}
