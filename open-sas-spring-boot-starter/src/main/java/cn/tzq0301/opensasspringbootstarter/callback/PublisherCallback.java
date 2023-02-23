package cn.tzq0301.opensasspringbootstarter.callback;

import cn.tzq0301.opensasspringbootstarter.common.MessageContent;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface PublisherCallback {
    void publish(@NonNull final MessageContent content);
}
