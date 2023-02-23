package cn.tzq0301.opensasmessagesprintbootstarter.net.util;

import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.Endpoint;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.WebSocketEndpointReference;
import com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public final class WebSocketEndpointReferenceUtils {
    @CheckReturnValue
    @NonNull
    public static String getMethod(@NonNull Class<?> clz) {
        checkNotNull(clz);
        WebSocketEndpointReference reference = checkNotNull(clz.getAnnotation(WebSocketEndpointReference.class));
        return WebSocketEndpointUtils.getMethod(reference.value());
    }

    @CheckReturnValue
    @NonNull
    public static String getMethod(@NonNull Endpoint endpoint) {
        checkNotNull(endpoint);
        return getMethod(endpoint.getClass());
    }
}
