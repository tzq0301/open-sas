package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint;

import cn.tzq0301.opensasmessagesprintbootstarter.net.common.payload.Payload;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.socket.WebSocketSession;

public interface Endpoint {
    void call(@Nullable final Payload payload, @NonNull final WebSocketSession session);
}
