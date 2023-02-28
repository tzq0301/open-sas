package cn.tzq0301.opensasspringbootstarter.channel;

import cn.tzq0301.opensasspringbootstarter.common.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

public interface Channel {
    void registerSubscriber(@NonNull final Group group,
                            @NonNull final Version version,
                            @NonNull final Priority priority,
                            @NonNull final Map<Topic, SubscriberCallback> topicToCallbackMap);

    void unregisterSubscriber(@NonNull final Group group,
                              @NonNull final Version version,
                              @NonNull final Priority priority);

    void publish(@NonNull final Group group,
                 @NonNull final Version version,
                 @NonNull final Priority priority,
                 @NonNull final Topic topic,
                 @NonNull final Message message);
}
