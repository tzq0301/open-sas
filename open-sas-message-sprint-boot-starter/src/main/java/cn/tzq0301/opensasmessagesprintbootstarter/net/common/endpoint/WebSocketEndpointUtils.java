package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint;

import com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public final class WebSocketEndpointUtils {
    @CheckReturnValue
    @NonNull
    public static String getMethod(@NonNull Class<?> clz) {
        checkNotNull(clz);

        if (clz.getAnnotation(WebSocketEndpointReference.class) != null) {
            return WebSocketEndpointReferenceUtils.getMethod(clz);
        }

        try {
            return clz.getAnnotation(WebSocketEndpoint.class).value();
        } catch (NullPointerException e) {
            return clz.getName();
        }
    }

    @CheckReturnValue
    @NonNull
    public static String getMethod(@NonNull Endpoint endpoint) {
        checkNotNull(endpoint);
        return getMethod(endpoint.getClass());
    }
}
