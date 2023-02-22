package cn.tzq0301.opensasmessagesprintbootstarter.callback;

import cn.tzq0301.opensasmessagesprintbootstarter.common.MessageContent;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface PublisherCallback {
    void publish(@NonNull final MessageContent content);
}
