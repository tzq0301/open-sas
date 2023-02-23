package cn.tzq0301.opensasmessagesprintbootstarter.common;

import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public record MessageContent(@NonNull Object content) {
    public MessageContent {
        checkNotNull(content);
    }
}
