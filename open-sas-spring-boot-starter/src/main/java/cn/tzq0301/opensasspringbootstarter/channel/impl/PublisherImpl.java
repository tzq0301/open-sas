package cn.tzq0301.opensasspringbootstarter.channel.impl;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.Publisher;
import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class PublisherImpl implements Publisher {
    private final Channel channel;

    private final Group group;

    private final Version version;

    private final Priority priority;

    public PublisherImpl(@NonNull final Channel channel,
                         @NonNull final Group group,
                         @NonNull final Version version,
                         @NonNull final Priority priority) {
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
    public void publish(@NonNull Message message) {
        channel.publish(group, version, priority, message);
    }
}
