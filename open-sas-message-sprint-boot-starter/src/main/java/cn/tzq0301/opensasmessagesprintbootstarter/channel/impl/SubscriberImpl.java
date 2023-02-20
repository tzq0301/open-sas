package cn.tzq0301.opensasmessagesprintbootstarter.channel.impl;

import cn.tzq0301.opensasmessagesprintbootstarter.channel.Channel;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.Subscriber;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.SubscriberCallback;
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
    public void onMessage(@NonNull final Channel channel, @NonNull final MessageContent content) {
        checkNotNull(channel);
        checkNotNull(content);
        callback.accept(content);
    }

    @Override
    public void subscribe(@NonNull final Channel channel) {
        checkNotNull(channel);
        channel.registerSubscriber(this);
    }

    @Override
    public void unsubscribe(@NonNull final Channel channel) {
        checkNotNull(channel);
        channel.unregisterSubscriber(this);
    }

    @Override
    public @NonNull Group group() {
        return group;
    }

    @Override
    public @NonNull Version version() {
        return version;
    }

    @Override
    public @NonNull Priority priority() {
        return priority;
    }
}
