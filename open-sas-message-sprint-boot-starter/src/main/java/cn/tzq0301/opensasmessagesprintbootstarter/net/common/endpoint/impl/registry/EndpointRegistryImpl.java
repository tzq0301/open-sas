package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.registry;

import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.Endpoint;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.EndpointRegistry;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.WebSocketEndpointUtils;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.payload.Payload;
import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class EndpointRegistryImpl implements EndpointRegistry {
    private final Map<String, Endpoint> registry;

    protected static final String METHOD_EXISTS_MESSAGE_TEMPLATE = "Method (%s) has been registered";

    protected static final String METHOD_NOT_FOUND_MESSAGE_TEMPLATE = "Method (%s) has not been registered";

    public EndpointRegistryImpl() {
        this.registry = Maps.newHashMap();
    }

    @Override
    public void register(@NonNull final Endpoint endpoint) {
        checkNotNull(endpoint);
        String method = WebSocketEndpointUtils.getMethod(endpoint);
        register(method, endpoint);
    }

    @Override
    public void register(@NonNull final String method, @NonNull final Endpoint endpoint) {
        checkNotNull(method);
        checkNotNull(endpoint);
        checkArgument(!registry.containsKey(method), METHOD_EXISTS_MESSAGE_TEMPLATE, method);
        registry.put(method, endpoint);
    }

    @Override
    public void call(@NonNull final Payload payload, @NonNull final WebSocketSession session) {
        checkNotNull(payload);
        checkNotNull(session);

        String method = payload.method();
        checkArgument(registry.containsKey(method), METHOD_NOT_FOUND_MESSAGE_TEMPLATE, method);
        registry.get(method).call(payload, session);
    }
}
