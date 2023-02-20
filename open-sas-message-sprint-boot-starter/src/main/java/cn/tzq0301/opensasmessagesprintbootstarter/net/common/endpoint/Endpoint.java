package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint;

import cn.tzq0301.opensasmessagesprintbootstarter.net.common.request.Request;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.response.Response;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.web.socket.WebSocketSession;

@FunctionalInterface
public interface Endpoint {
    Response<?> call(@NonNull Request<?> request, @NonNull WebSocketSession session);
}
