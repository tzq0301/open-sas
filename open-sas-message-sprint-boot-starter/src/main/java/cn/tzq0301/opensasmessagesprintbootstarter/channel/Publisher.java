package cn.tzq0301.opensasmessagesprintbootstarter.channel;

import cn.tzq0301.opensasmessagesprintbootstarter.core.MessageContent;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Publisher {
    void publish(@NonNull final Channel channel, @NonNull final MessageContent content);
}
