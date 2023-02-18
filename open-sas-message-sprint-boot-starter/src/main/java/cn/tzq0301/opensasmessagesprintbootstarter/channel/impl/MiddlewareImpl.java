package cn.tzq0301.opensasmessagesprintbootstarter.channel.impl;

import cn.tzq0301.opensasmessagesprintbootstarter.channel.Channel;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.Middleware;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.MiddlewareCallback;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.SubscriberCallback;
import cn.tzq0301.opensasmessagesprintbootstarter.core.*;
import com.google.common.base.Preconditions;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public record MiddlewareImpl(@NonNull Group group, @NonNull Version version, @NonNull Priority priority,
                             @NonNull MiddlewareCallback callback) implements Middleware {
    public MiddlewareImpl {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(callback);
    }

    @Override
    public void publish(@NonNull final Channel channel, @NonNull final MessageContent content) {
        checkNotNull(channel);
        checkNotNull(content);
        channel.publish(new Message(group, version, Priorities.cloneByDownGrade(priority), content));
    }

    @Override
    public void onMessage(@NonNull final Channel channel, @NonNull final MessageContent content) {
        checkNotNull(content);
        callback.apply(content).ifPresent(c -> this.publish(channel, c));
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
