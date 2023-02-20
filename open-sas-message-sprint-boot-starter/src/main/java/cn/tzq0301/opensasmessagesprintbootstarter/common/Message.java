package cn.tzq0301.opensasmessagesprintbootstarter.common;

import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public record Message(@NonNull Group group, @NonNull Version version, @NonNull Priority priority, @NonNull MessageContent content) {
    public Message {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(content);
    }
}
