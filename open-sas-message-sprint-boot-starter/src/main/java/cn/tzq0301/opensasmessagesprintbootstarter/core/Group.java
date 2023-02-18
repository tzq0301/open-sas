package cn.tzq0301.opensasmessagesprintbootstarter.core;

import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public record Group(@NonNull String group) {
    public Group {
        checkNotNull(group);
    }

    public static Group of(@NonNull final String group) {
        checkNotNull(group);
        return new Group(group);
    }
}
