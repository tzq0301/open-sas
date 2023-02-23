package cn.tzq0301.opensasspringbootstarter.channel.impl;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.Publisher;
import cn.tzq0301.opensasspringbootstarter.common.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public final class PublisherImpl implements Publisher {
    private final Channel channel;

    private final Group group;

    private final Version version;

    private final Priority priority;

    public PublisherImpl(@NonNull Channel channel, @NonNull Group group, @NonNull Version version, @NonNull Priority priority) {
        checkNotNull(channel);
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        this.channel = channel;
        this.group = group;
        this.version = version;
        this.priority = priority;
    }

    @Override
    public void publish(@NonNull final MessageContent content) {
        checkNotNull(content);
        channel.publish(new Message(group, version, priority, content));
    }

}
