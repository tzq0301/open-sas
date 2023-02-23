package cn.tzq0301.opensasmessagesprintbootstarter.net.common.payload;

import cn.tzq0301.opensasmessagesprintbootstarter.net.util.WebSocketEndpointReferenceUtils;
import com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public record Payload(@NonNull String method, @Nullable Object data) {
    public Payload {
        checkNotNull(method);
    }

    @CheckReturnValue
    @NonNull
    public static Payload fromData(@NonNull Object data) {
        checkNotNull(data);
        return new Payload(WebSocketEndpointReferenceUtils.getMethod(data.getClass()), data);
    }
}
