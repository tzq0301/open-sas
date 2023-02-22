package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.unregister;

import cn.tzq0301.opensasmessagesprintbootstarter.common.Group;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Priority;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Version;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.WebSocketEndpointReference;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

@WebSocketEndpointReference(Unregister.class)
public record UnregisterRequest(@NonNull Group group, @NonNull Version version, @NonNull Priority priority) {
    public UnregisterRequest {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
    }
}
