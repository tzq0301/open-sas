package cn.tzq0301.opensasmessagesprintbootstarter.net.common.payload;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public record Payload<T>(@NonNull String method, @Nullable T data) {
    public Payload {
        checkNotNull(method);
    }
}
