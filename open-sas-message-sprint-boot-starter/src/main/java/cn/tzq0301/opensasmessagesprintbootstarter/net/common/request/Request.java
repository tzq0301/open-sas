package cn.tzq0301.opensasmessagesprintbootstarter.net.common.request;

import cn.tzq0301.opensasmessagesprintbootstarter.net.common.payload.Payload;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public record Request<T>(@NonNull Payload<T> payload) {
    public Request {
        checkNotNull(payload);
    }
}
