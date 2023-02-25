package cn.tzq0301.opensasspringbootstarter.channel;

import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Channel {
    void registerSubscriber(@NonNull final Group group,
                            @NonNull final Version version,
                            @NonNull final Priority priority,
                            @NonNull final SubscriberCallback subscriber);

    void unregisterSubscriber(@NonNull final Group group,
                              @NonNull final Version version,
                              @NonNull final Priority priority);

    void publish(@NonNull final Group group,
                 @NonNull final Version version,
                 @NonNull final Priority priority,
                 @NonNull final Message message);
}
