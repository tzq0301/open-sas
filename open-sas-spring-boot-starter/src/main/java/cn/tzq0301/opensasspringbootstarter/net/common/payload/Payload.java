package cn.tzq0301.opensasspringbootstarter.net.common.payload;

import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
import cn.tzq0301.opensasspringbootstarter.net.util.WebSocketEndpointReferenceUtils;
import com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public record Payload(@NonNull String method,
                      @NonNull Group group,
                      @NonNull Version version,
                      @NonNull Priority priority,
                      @Nullable Object data) {
    public Payload {
        checkNotNull(method);
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
    }

    @CheckReturnValue
    @NonNull
    public static Payload fromData(@NonNull Group group,
                                   @NonNull Version version,
                                   @NonNull Priority priority,
                                   @NonNull Object data) {
        checkNotNull(data);
        return new Payload(WebSocketEndpointReferenceUtils.getMethod(data.getClass()), group, version, priority, data);
    }
}
