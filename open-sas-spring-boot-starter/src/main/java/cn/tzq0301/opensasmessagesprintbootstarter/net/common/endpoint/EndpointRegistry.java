package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint;

import cn.tzq0301.opensasmessagesprintbootstarter.net.common.payload.Payload;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.socket.WebSocketSession;

public interface EndpointRegistry {
    void call(@NonNull final Payload payload, @NonNull final WebSocketSession session);

    void register(@NonNull final Endpoint endpoint);

    void register(@NonNull final String name, @NonNull final Endpoint endpoint);
}
