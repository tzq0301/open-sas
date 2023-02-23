package cn.tzq0301.opensasmessagesprintbootstarter.callback;

import cn.tzq0301.opensasmessagesprintbootstarter.common.MessageContent;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface MiddlewareCallback {
    void onMessage(@NonNull final MessageContent content, @NonNull final PublisherCallback publisher);
}
