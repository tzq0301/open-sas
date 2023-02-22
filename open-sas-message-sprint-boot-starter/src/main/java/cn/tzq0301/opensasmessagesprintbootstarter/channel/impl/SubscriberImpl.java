package cn.tzq0301.opensasmessagesprintbootstarter.channel.impl;

import cn.tzq0301.opensasmessagesprintbootstarter.callback.SubscriberCallback;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.Channel;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.Subscriber;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Group;
import cn.tzq0301.opensasmessagesprintbootstarter.common.MessageContent;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Priority;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public record SubscriberImpl(@NonNull Group group, @NonNull Version version, @NonNull Priority priority,
                             @NonNull SubscriberCallback callback) implements Subscriber {
    public SubscriberImpl {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(callback);
    }

    @Override
    public void onMessage(@NonNull final MessageContent content) {
        checkNotNull(content);
        callback.onMessage(content);
    }

    @Override
    public void subscribe(@NonNull Channel channel) {
        checkNotNull(channel);
        channel.registerSubscriber(group, version, priority, callback);
    }

    @Override
    public void unsubscribe(@NonNull Channel channel) {
        checkNotNull(channel);
        channel.unregisterSubscriber(group, version, priority);
    }
}
