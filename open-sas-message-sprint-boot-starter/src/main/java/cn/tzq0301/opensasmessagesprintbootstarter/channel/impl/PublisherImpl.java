package cn.tzq0301.opensasmessagesprintbootstarter.channel.impl;

import cn.tzq0301.opensasmessagesprintbootstarter.channel.Channel;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.Publisher;
import cn.tzq0301.opensasmessagesprintbootstarter.common.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public record PublisherImpl(@NonNull Channel channel, @NonNull Group group,
                            @NonNull Version version, @NonNull Priority priority) implements Publisher {
    public PublisherImpl {
        checkNotNull(channel);
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
    }

    @Override
    public void publish(@NonNull final MessageContent content) {
        checkNotNull(channel);
        checkNotNull(content);
        channel.publish(new Message(group, version, priority, content));
    }
}
