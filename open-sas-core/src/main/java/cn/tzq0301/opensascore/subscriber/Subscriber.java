package cn.tzq0301.opensascore.subscriber;

import cn.tzq0301.opensascore.channel.Channel;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Subscriber extends SubscriberCallback {
    void subscribe(@NonNull Channel channel);

    void unsubscribe(@NonNull Channel channel);
}
