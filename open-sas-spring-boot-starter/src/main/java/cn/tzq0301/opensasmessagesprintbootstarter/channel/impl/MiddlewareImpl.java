package cn.tzq0301.opensasmessagesprintbootstarter.channel.impl;

import cn.tzq0301.opensasmessagesprintbootstarter.callback.MiddlewareCallback;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.Channel;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.Middleware;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.Publisher;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.Subscriber;
import cn.tzq0301.opensasmessagesprintbootstarter.common.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public final class MiddlewareImpl implements Middleware {
    private final Publisher publisher;

    private final Subscriber subscriber;

    public MiddlewareImpl(@NonNull final Channel channel, @NonNull final Group group,
                          @NonNull final Version version, @NonNull final Priority priority,
                          @NonNull final MiddlewareCallback callback) {
        checkNotNull(channel);
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(callback);
        this.publisher = content -> channel.publish(new Message(group, version, Priorities.cloneByDownGrade(priority), content));
        this.subscriber = new SubscriberImpl(group, version, priority, content -> callback.onMessage(content, this.publisher));
    }

    @Override
    public void publish(@NonNull final MessageContent content) {
        checkNotNull(content);
        publisher.publish(content);
    }

    @Override
    public void onMessage(@NonNull final MessageContent content) {
        checkNotNull(content);
        subscriber.onMessage(content);
    }

    @Override
    public void subscribe(@NonNull final Channel channel) {
        checkNotNull(channel);
        subscriber.subscribe(channel);
    }

    @Override
    public void unsubscribe(@NonNull final Channel channel) {
        checkNotNull(channel);
        subscriber.unsubscribe(channel);
    }
}
